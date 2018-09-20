package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public User addUser(User user){
        return userRepository.save(user);
    }
}
