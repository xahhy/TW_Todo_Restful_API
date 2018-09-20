package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionService {
    Map<String, User> sessionRepository;

    public SessionService() {
        this.sessionRepository = new HashMap<>();
    }


    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionRepository.put(sessionId, user);
        return sessionId;
    }

    public User getSessionUser(String sessionId) {
        return sessionRepository.get(sessionId);
    }
}
