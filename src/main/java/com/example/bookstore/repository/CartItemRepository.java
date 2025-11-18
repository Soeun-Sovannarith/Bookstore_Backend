package com.example.bookstore.repository;

import com.example.bookstore.model.Cart_Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<Cart_Item, Integer> {
    List<Cart_Item> findByUserId(Integer userId);
    List<Cart_Item> findByBookId(Integer bookId);
}

