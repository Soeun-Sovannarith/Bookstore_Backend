package com.example.bookstore.model;

import java.util.Date;
import java.util.UUID;

public class Cart_Item {
    private UUID id;
    private UUID userID;
    private UUID bookID;
    private int quantity;
    private Date createda_at;

    public Cart_Item() {}

    public Cart_Item(UUID id, UUID userID, UUID bookID, int quantity, Date createda_at) {
        this.id = id;
        this.userID = userID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.createda_at = createda_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
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

    public Date getCreateda_at() {
        return createda_at;
    }

    public void setCreateda_at(Date createda_at) {
        this.createda_at = createda_at;
    }
}
