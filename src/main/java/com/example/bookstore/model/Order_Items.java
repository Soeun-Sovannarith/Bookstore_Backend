package com.example.bookstore.model;

//import java.util.Integer;

public class Order_Items {
    private Integer id;
    private Integer orderID;
    private Integer bookID;
    private int quantity;
    private float price;

    public Order_Items() {}

    public Order_Items(Integer id, Integer orderID, Integer bookID, int quantity, float price) {
        this.id = id;
        this.orderID = orderID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
