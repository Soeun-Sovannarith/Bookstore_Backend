/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.bookstore.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.User;





/**
 *
 * @author ppc
 */

@Controller
public class HelloController {

        private final List<User> users = Arrays.asList(
            new User(1, "Alice", "Smith", "alice@example.com"),
            new User(2, "Bob", "Johnson", "bob@example.com"),
            new User(3, "Ryan", "Holiday", "ryan@gmail.com")
        );


        private final List<Book> book = Arrays.asList(
            new Book(1, "Atomic habit", "Jame Clear", "2018"),
            new Book(2, "The Miracle Morning", "Hal Elrod", "2016"),
            new Book(3, "The Romance of Three Kingdom", "Luo Guanzhong", "1124")

        ); 

    @GetMapping("/api/usr")
    @ResponseBody
    public List <User> getuser() {
        return users;
    }

    @GetMapping("/api/books")
    @ResponseBody
    public List<Book> getBook() {
        return book; 
    }
    
    

    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("books", book);
        model.addAttribute("users", users);
        return "index";
    }

   

    
    

}
