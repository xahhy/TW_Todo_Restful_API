package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {


    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getTodos(@RequestParam(required = false) List<String> tags){
        System.out.println(tags);
        return todoService.getTodoList();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    public Todo addTodo(@RequestBody Todo todo){
        return todoService.addTodo(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id){
        todoService.deleteTodo(id);
    }

    @PutMapping("/{id}")
    public Todo setTodo(@PathVariable Long id, @RequestBody Todo todo){
        return todoService.updateTodo(id, todo);
    }

}
