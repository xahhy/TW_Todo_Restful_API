package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.persist.TagRepository;
import com.thoughtworks.training.restfulapi.persist.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Todo> getTodoListByName(String name, Pageable pageable) {
        return todoRepository.findAllByNameContaining(name, pageable);
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
                todo.getTags().stream().map(tag -> tagService.save(tag)).collect(Collectors.toSet())
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


    public Page<Todo> getPageableTodoList(Pageable pageable) {
        User user = TokenService.getPrincipal();
        return todoRepository.findAllByUser_Id(user.getId(), pageable);
    }

    public Todo getTodoById(User user, Long id) {
        return todoRepository.findOneByUser_IdAndId(id, user.getId());
    }
}
