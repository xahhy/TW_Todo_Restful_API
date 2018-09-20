package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.persist.TagRepository;
import com.thoughtworks.training.restfulapi.persist.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Page<Todo> getTodoListByName(String name, Pageable pageable){
        return todoRepository.findAllByNameContaining(name, pageable);
    }

    public Todo getTodoById(long id) {
        Todo todo = todoRepository.findOne(id);
        if (todo != null) {
            return todo;
        } else {
            throw new NotFoundException();
        }

    }

    public Todo addTodo(Todo todo) {
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
        try {

            todoRepository.delete(id);
        } catch (Exception exception) {
            throw new NotFoundException();
        }
        return true;
    }

    public Todo updateTodo(Todo newTodo) {
        bindTags(newTodo);
        return todoRepository.save(newTodo);
    }

    public Page<Todo> getPageableTodoList(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }
}
