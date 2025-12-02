package com.example.bookstore.dto;

public class StripePaymentRequest {
    private Long orderId;
    private Long amount; // Amount in cents (e.g., 1000 = $10.00)
    private String currency; // e.g., "usd"
    private String description;
    private String receiptEmail;

    public StripePaymentRequest() {}

    public StripePaymentRequest(Long orderId, Long amount, String currency, String description, String receiptEmail) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.receiptEmail = receiptEmail;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    public void setReceiptEmail(String receiptEmail) {
        this.receiptEmail = receiptEmail;
    }
}

