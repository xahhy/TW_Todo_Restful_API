package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    Map<String, User> tokenRepository;

    public TokenService() {
        this.tokenRepository = new HashMap<>();
    }


    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        tokenRepository.put(sessionId, user);
        return sessionId;
    }

    public User getUserFromToken(String sessionId) {
        return tokenRepository.get(sessionId);
    }

    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
