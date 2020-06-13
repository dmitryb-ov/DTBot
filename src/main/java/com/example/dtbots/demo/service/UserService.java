package com.example.dtbots.demo.service;

import com.example.dtbots.demo.models.User;

import java.util.Optional;

public interface UserService {
    void addUser(User user);
    Optional<User> getUserBySystemId(Long systemId);
    Optional<User> getUserById(Long id);
}
