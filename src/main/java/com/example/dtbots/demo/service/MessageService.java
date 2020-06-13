package com.example.dtbots.demo.service;

import com.example.dtbots.demo.models.Message;

import java.util.Optional;

public interface MessageService {
    void addMessage(Message message);
    Optional<Message> getLastMessage();
}
