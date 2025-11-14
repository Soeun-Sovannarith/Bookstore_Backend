/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.bookstore.model;

/**
 *
 * @author ppc
 */

public class Book {
    private int bookID; 
    private String title; 
    private String author; 
    private String published_date;
    
    public Book(int bookID, String title, String author, String published_date) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.published_date = published_date;
    }
    public int getBookID() {
        return bookID;
    }
    public void setBookID(int bookID) {
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
    

}
