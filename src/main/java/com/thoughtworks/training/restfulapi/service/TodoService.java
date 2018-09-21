package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.model.TodoSearcher;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.persist.TagRepository;
import com.thoughtworks.training.restfulapi.persist.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private TodoRepository todoRepository;
    @Autowired
    private TagService tagService;

    private TagRepository tagRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, TagRepository tagRepository) {
        this.todoRepository = todoRepository;
        this.tagRepository = tagRepository;
    }

    public List<Todo> getTodoList() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(long id) {
        User user = TokenService.getPrincipal();
        Todo todo = todoRepository.findOneByUser_IdAndId(user.getId(), id);
        if (todo == null) {
            throw new NotFoundException();
        }
        return todo;
    }

    public Todo addTodo(Todo todo) {
        User user = TokenService.getPrincipal();
        todo.setUser(user);
        bindTags(todo);
        todoRepository.save(todo);
        return todo;
    }

    private void bindTags(Todo todo) {
        todo.setTags(
                todo.getTags().stream().map(tag -> tagService.getTagById(tag)).filter(Objects::nonNull).collect(Collectors.toList())
        );
    }

    public Boolean deleteTodo(Long id) {
        User user = TokenService.getPrincipal();
        if (todoRepository.findOneByUser_IdAndId(user.getId(), id) == null) {
            throw new NotFoundException();
        }
        todoRepository.deleteByUser_IdAndId(user.getId(), id);
        return true;
    }

    public Todo updateTodo(Long id, Todo newTodo) {
        User user = TokenService.getPrincipal();
        Todo _todo = todoRepository.findOneByUser_IdAndId(user.getId(), id);
        if (_todo == null) {
            throw new NotFoundException();
        }
        newTodo.setId(id).setUser(user);
        bindTags(newTodo);
        return todoRepository.save(newTodo);
    }


    public Page<Todo> getPageableTodoList(TodoSearcher todoSearcher, Pageable pageable) {
        User user = TokenService.getPrincipal();
        return todoRepository.findAll(getWhereClause(todoSearcher), pageable);
    }

    private Specification<Todo> getWhereClause(final TodoSearcher todoSearcher) {
        User user = TokenService.getPrincipal();
        return new Specification<Todo>() {
            @Override
            public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (todoSearcher.getName() != null) {
                    predicate.add(cb.like(root.get("name").as(String.class), "%" + todoSearcher.getName() + "%"));
                }
                if (todoSearcher.getStartDate() != null || todoSearcher.getEndDate() != null) {
                    if (todoSearcher.getStartDate() != null) {
                        predicate.add(cb.greaterThanOrEqualTo(root.get("dueDate").as(Date.class), todoSearcher.getStartDate()));
                    }
                    if (todoSearcher.getEndDate() != null) {
                        predicate.add(cb.lessThanOrEqualTo(root.get("dueDate").as(Date.class), todoSearcher.getEndDate()));
                    }
                }
                if (todoSearcher.getTagsId() != null) {
                    todoSearcher.getTagsId().stream().forEach(
                            tagId -> predicate.add(
                                    root.<Todo, Tag>joinList("tags").get("id").in(tagId)
                            )
                    );
                }
                predicate.add(cb.equal(root.<User>get("user").as(User.class), user));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }
}
