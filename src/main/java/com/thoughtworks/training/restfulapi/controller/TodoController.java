package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.model.TodoSearcher;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.service.TokenService;
import com.thoughtworks.training.restfulapi.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {


    @Autowired
    private TodoService todoService;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public Page<Todo> getTodos(@ModelAttribute TodoSearcher todoSearcher, Pageable pageable) {
        return todoService.getPageableTodoList(todoSearcher, pageable);
//        if (name != null) {
//            return todoService.getTodoListByName(name, pageable);
//        }
//        return todoService.getPageableTodoList(pageable);
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    @PostMapping
    public Todo addTodo(@RequestBody Todo todo) {
        return todoService.addTodo(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

    @PutMapping("/{id}")
    public Todo setTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    private Optional<User> getCurrentUser(HttpServletRequest request) {
        Optional<User> currentUser = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .filter(cookie -> tokenService.getUserFromToken(cookie.getValue()) != null)
                .map(cookie -> tokenService.getUserFromToken(cookie.getValue()))
                .findFirst();
        if (!currentUser.isPresent()) throw new NotFoundException();
        return currentUser;
    }

}
