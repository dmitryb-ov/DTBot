package com.example.dtbots.demo.repositories;

import com.example.dtbots.demo.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    Optional<Message> findFirstByOrderByIdDesc();
}
