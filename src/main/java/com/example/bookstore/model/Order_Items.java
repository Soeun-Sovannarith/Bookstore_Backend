package com.example.bookstore.model;

import java.util.UUID;

public class Order_Items {
    private UUID id;
    private UUID orderID;
    private UUID bookID;
    private int quantity;
    private float price;

    public Order_Items() {}

    public Order_Items(UUID id, UUID orderID, UUID bookID, int quantity, float price) {
        this.id = id;
        this.orderID = orderID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getBookID() {
        return bookID;
    }

    public void setBookID(UUID bookID) {
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
