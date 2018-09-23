package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.service.TokenService;
import com.thoughtworks.training.restfulapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody User user){
        String token;
        User loginUser = userService.getUser(user);
        if (loginUser != null){
            token = tokenService.createToken(loginUser);
            return ResponseEntity.ok().body(token);
        }else {
            return ResponseEntity.notFound().build();
        }

    }
}
