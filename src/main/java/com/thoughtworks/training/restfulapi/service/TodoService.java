package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Todo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoService {
    private Map<Long, Todo> todoList = new HashMap<>();

    public TodoService() {
        todoList.put(1L, new Todo(1L, "meeting", "To Do", new Date()));
        todoList.put(2L, new Todo(2L, "meeting with LY", "To Do", new Date()));
        todoList.put(3L, new Todo(3L, "learn", "In progress", new Date()));
        todoList.put(4L, new Todo(4L, "preparation", "Finished", new Date()));
    }

    public List<Todo> getTodoList() {
        return new ArrayList<Todo>(todoList.values());
    }

    public Todo getTodoById(long id) {
        Todo todo = todoList.get(id);
        if (todo != null){
            return todo;
        }else {
            throw new NotFoundException();
        }

    }

    public Todo addTodo(Todo todo) {
        todo.setId(UUID.randomUUID().getMostSignificantBits());
        todoList.put(todo.getId(), todo);
        return todo;
    }

    public void deleteTodo(Long id) {
        try {
            todoList.remove(id);
        } catch (Exception exception) {
            throw new NotFoundException();
        }
    }

    public Todo updateTodo(Long id, Todo newTodo) {
        todoList.put(id, newTodo);
        return todoList.get(id);
    }
}
