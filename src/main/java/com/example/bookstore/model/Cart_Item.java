package com.example.bookstore.model;

import java.util.Date;
//import java.util.Integer;

public class Cart_Item {
    private Integer id;
    private Integer userID;
    private Integer bookID;
    private int quantity;
    private Date createda_at;

    public Cart_Item() {}

    public Cart_Item(Integer id, Integer userID, Integer bookID, int quantity, Date createda_at) {
        this.id = id;
        this.userID = userID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.createda_at = createda_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getCreateda_at() {
        return createda_at;
    }

    public void setCreateda_at(Date createda_at) {
        this.createda_at = createda_at;
    }
}
