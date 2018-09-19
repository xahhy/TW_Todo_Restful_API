package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.persist.TodoRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    private Map<Long, Todo> todoList = new HashMap<>();

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
//        todoRepository.save(new Todo(1L, "meeting new", "To Do", new Date()));
//        todoRepository.save(new Todo(2L, "meeting with LY", "To Do", new Date()));
//        todoRepository.save(new Todo(3L, "learn", "In progress", new Date()));
//        todoRepository.save(new Todo(4L, "preparation", "Finished", new Date()));
    }

    public List<Todo> getTodoList() {
        return todoRepository.findAll();
    }

    public List<Todo> getTodoListByName(String name){
        return todoRepository.findAllByNameContaining(name);
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
        todoRepository.save(todo);
        return todo;
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
        return todoRepository.save(newTodo);
    }
}
