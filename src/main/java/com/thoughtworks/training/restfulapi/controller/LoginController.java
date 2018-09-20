package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.service.SessionService;
import com.thoughtworks.training.restfulapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @PostMapping
    public ResponseEntity login(@RequestBody User user,  HttpServletResponse res){
        String sessionId;
        if (userService.validateUser(user)){
            HttpHeaders headers = new HttpHeaders();
            sessionId = sessionService.createSession(user);
            res.addCookie(new Cookie("sessionId", sessionId));
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }
}
