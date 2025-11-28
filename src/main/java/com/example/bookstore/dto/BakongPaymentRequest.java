package com.example.bookstore.dto;

public class BakongPaymentRequest {
    private Long orderId;
    private Double amount;
    private String currency; // "USD" or "KHR"
    private String billNumber;

    // Constructors
    public BakongPaymentRequest() {}

    public BakongPaymentRequest(Long orderId, Double amount, String currency, String billNumber) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.billNumber = billNumber;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
}

