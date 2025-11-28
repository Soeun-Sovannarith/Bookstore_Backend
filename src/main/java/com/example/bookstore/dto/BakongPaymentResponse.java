package com.example.bookstore.dto;

public class BakongPaymentResponse {
    private String qrCode;      // Base64 encoded QR code or KHQR string
    private String md5;         // MD5 hash for verification
    private String billNumber;
    private Double amount;
    private String currency;

    // Constructors
    public BakongPaymentResponse() {}

    public BakongPaymentResponse(String qrCode, String md5, String billNumber, Double amount, String currency) {
        this.qrCode = qrCode;
        this.md5 = md5;
        this.billNumber = billNumber;
        this.amount = amount;
        this.currency = currency;
    }

    // Getters and Setters
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
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
}
