/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.bookstore.model;

import java.util.UUID;

/**
 *
 * @author ppc
 */

public class Book {
    private UUID bookID;
    private String title;
    private String author;
    private String published_date;
    private int stock;
    private String category;
    private double price;
    private String description;

    public Book(UUID bookID, String title, String author, String published_date, int stock, String category, double price, String description, String imageURL) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.published_date = published_date;
        this.stock = stock;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
    }

    private String imageURL;

    public UUID getBookID() {
        return bookID;
    }

    public void setBookID(UUID bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
    

