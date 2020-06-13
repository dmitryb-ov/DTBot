package com.example.dtbots.demo.service;

import com.example.dtbots.demo.models.User;
import com.example.dtbots.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserBySystemId(Long systemId) {
        return userRepository.findUserBySystemId(systemId);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findUserById(id);
    }
}
