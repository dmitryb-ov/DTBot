package com.example.dtbots.demo.service;

import com.example.dtbots.demo.models.Message;
import com.example.dtbots.demo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Optional<Message> getLastMessage() {
        return messageRepository.findFirstByOrderByIdDesc();
    }
}
