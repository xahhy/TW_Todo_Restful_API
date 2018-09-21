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

    public User getUser(User user) {
        User oldUser = userRepository.findByName(user.getName());
        if (oldUser != null){
            return oldUser.getPassword().equals(user.getPassword()) ? oldUser : null;
        }else {
            return null;
        }
    }

    public User getUserById(Long id){
        return userRepository.findOne(id);
    }
}
