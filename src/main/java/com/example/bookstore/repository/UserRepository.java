package com.example.bookstore.repository;

import com.example.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.Integer;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
