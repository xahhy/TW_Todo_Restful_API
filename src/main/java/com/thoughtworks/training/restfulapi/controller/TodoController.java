package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.service.SessionService;
import com.thoughtworks.training.restfulapi.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoController {


    @Autowired
    private TodoService todoService;

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public Page<Todo> getTodos(
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String name,
            Pageable pageable,
            HttpServletRequest request
    ) {
        Optional<User> User = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .map(cookie -> sessionService.getSessionUser(cookie.getValue()))
                .findFirst();
        if (User.isPresent()){
            return todoService.getPageableTodoList(User.get(), pageable);
        }
        if (name != null) {
            return todoService.getTodoListByName(name, pageable);
        }
        return todoService.getPageableTodoList(pageable);
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
        todo.setId(id);
        return todoService.updateTodo(todo);
    }

}
