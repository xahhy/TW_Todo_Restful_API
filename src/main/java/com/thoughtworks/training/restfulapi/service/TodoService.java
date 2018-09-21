package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
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

    private Map<Long, Todo> todoList = new HashMap<>();

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
        if (_todo == null){
            throw new NotFoundException();
        }
        newTodo.setId(id).setUser(user);
        bindTags(newTodo);
        return todoRepository.save(newTodo);
    }


    public Page<Todo> getPageableTodoList(TodoSearcher todoSearcher, Pageable pageable) {
        User user = TokenService.getPrincipal();
        return todoRepository.findAll(getWhereClause(todoSearcher), pageable);
//        return todoRepository.findAllByUser_Id(user.getId(), pageable);
    }

    public Todo getTodoById(User user, Long id) {
        return todoRepository.findOneByUser_IdAndId(id, user.getId());
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
                predicate.add(cb.equal(root.<User>get("user").as(User.class), user));
//                if(todoSearcher.getPostTimeEnd()!=null){
//                    predicate.add(cb.lessThanOrEqualTo(root.get("postTime").as(Date.class), todoSearcher.getPostTimeEnd()));
//                }
//                if(todoSearcher.getRecTimeStart()!=null){
//                    predicate.add(cb.greaterThanOrEqualTo(root.get("recommendTime").as(Date.class), todoSearcher.getRecTimeStart()));
//                }
//                if (todoSearcher.getRecTimeEnd()!=null){
//                    predicate.add(cb.lessThanOrEqualTo(root.get("recommendTime").as(Date.class), todoSearcher.getRecTimeEnd()));
//                }
//                if (StringUtils.isNotBlank(todoSearcher.getNickname())){
//                    //两张表关联查询
//                    Join<Todo,User> userJoin = root.join(root.getModel().getSingularAttribute("user",User.class),JoinType.LEFT);
//                    predicate.add(cb.like(userJoin.get("nickname").as(String.class), "%" + todoSearcher.getNickname() + "%"));
//                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }
}
