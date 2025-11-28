package com.example.bookstore.service;

import com.example.bookstore.dto.BakongPaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class BakongPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(BakongPaymentService.class);
    private static final int QR_EXPIRATION_MINUTES = 15; // QR code valid for 15 minutes

    @Value("${bakong.account.id}")
    private String bakongAccountId;

    @Value("${bakong.account.phone}")
    private String accountPhone;

    @Value("${bakong.acquiring.bank}")
    private String acquiringBank;

    @Value("${bakong.merchant.name}")
    private String merchantName;

    @Value("${bakong.merchant.city}")
    private String merchantCity;

    @Value("${bakong.store.label}")
    private String storeLabel;

    @Value("${bakong.terminal.label}")
    private String terminalLabel;

    @Value("${bakong.api.token}")
    private String bakongApiToken;

    /**
     * Generate Bakong KHQR code for payment (MOCK IMPLEMENTATION)
     *
     * NOTE: This is a mock implementation for testing purposes.
     * To use the real Bakong SDK, you need to:
     * 1. Obtain the Bakong SDK JAR from NBC or your payment provider
     * 2. Install it in your local Maven repository
     * 3. Uncomment the Bakong dependency in pom.xml
     * 4. Replace this method with the real SDK implementation
     *
     * @param billNumber Unique bill/order number
     * @param amount Payment amount
     * @param currency "USD" or "KHR"
     * @return BakongPaymentResponse containing QR code data
     */
    public BakongPaymentResponse generateQRCode(String billNumber, Double amount, String currency) {
        try {
            logger.info("⚠️ MOCK MODE: Starting Bakong QR generation - Bill: {}, Amount: {}, Currency: {}",
                billNumber, amount, currency);
            logger.warn("This is a MOCK implementation. Real Bakong SDK is not installed.");

            // Calculate expiration timestamp (15 minutes from now)
            LocalDateTime expirationTime = LocalDateTime.now(ZoneId.of("Asia/Phnom_Penh"))
                .plusMinutes(QR_EXPIRATION_MINUTES);
            String expirationTimestamp = expirationTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            logger.info("QR code will expire at: {} (timestamp: {})", expirationTime, expirationTimestamp);

            // Generate mock KHQR string (simplified format for testing)
            String mockQRString = generateMockKHQRString(billNumber, amount, currency, expirationTimestamp);

            // Generate mock MD5 hash
            String md5Hash = generateMD5(mockQRString);

            logger.info("✅ MOCK: Bakong QR code generated successfully");

            BakongPaymentResponse paymentResponse = new BakongPaymentResponse();
            paymentResponse.setQrCode(mockQRString);
            paymentResponse.setMd5(md5Hash);
            paymentResponse.setBillNumber(billNumber);
            paymentResponse.setAmount(amount);
            paymentResponse.setCurrency(currency);

            return paymentResponse;

        } catch (Exception e) {
            logger.error("Exception during mock Bakong QR generation", e);
            throw new RuntimeException("Error generating mock Bakong QR code: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a simplified mock KHQR string for testing
     */
    private String generateMockKHQRString(String billNumber, Double amount, String currency, String expiration) {
        // This is a simplified mock. Real KHQR follows EMV QR Code specification
        StringBuilder qr = new StringBuilder();
        qr.append("00020101"); // Payload Format Indicator
        qr.append("010211"); // Point of Initiation Method
        qr.append("29370016khqr.com"); // Merchant Account Information
        qr.append("0116").append(bakongAccountId); // Bakong Account ID
        qr.append("0103").append(currency); // Currency
        qr.append("54").append(String.format("%04d", String.valueOf(amount).length())).append(amount); // Amount
        qr.append("5802KH"); // Country Code
        qr.append("59").append(String.format("%02d", merchantName.length())).append(merchantName); // Merchant Name
        qr.append("60").append(String.format("%02d", merchantCity.length())).append(merchantCity); // Merchant City
        qr.append("62").append(String.format("%02d", billNumber.length())).append(billNumber); // Bill Number
        qr.append("6304"); // CRC placeholder

        return Base64.getEncoder().encodeToString(qr.toString().getBytes());
    }

    /**
     * Generate MD5 hash for verification
     */
    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error("Error generating MD5 hash", e);
            return "mock-md5-" + System.currentTimeMillis();
        }
    }
}

