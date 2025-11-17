package com.example.bookstore.model;

import javax.xml.crypto.Data;


//@Entity
public class Order {
//    @Id
//    @GeneratedValue
    private Integer id;
    private Integer userId;
    private float totalAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String ShippingAddress;
    private Data createdAt;

    public Order() {}

    public Order(Integer id, Integer userId, float totalAmount, String status, String paymentMethod, String paymentStatus, String shippingAddress, Data createdAt) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        ShippingAddress = shippingAddress;
        this.createdAt = createdAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public Data getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Data createdAt) {
        this.createdAt = createdAt;
    }


}
