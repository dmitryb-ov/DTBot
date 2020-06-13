package com.example.dtbots.demo.repositories;

import com.example.dtbots.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserBySystemId(Long systemId);
    Optional<User> findUserById(Long userId);
}
