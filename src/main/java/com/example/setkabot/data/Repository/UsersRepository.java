package com.example.setkabot.data.Repository;

import com.example.setkabot.data.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByChatId(String chatId);
    Users findByUserid(String userId);
}