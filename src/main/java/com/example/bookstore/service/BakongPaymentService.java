package com.example.bookstore.service;

import com.example.bookstore.dto.BakongPaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

@Service
public class BakongPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(BakongPaymentService.class);

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

    @PostConstruct
    public void validateConfiguration() {
        if (bakongAccountId == null || bakongAccountId.isEmpty()) {
            throw new IllegalStateException("Bakong account ID is not configured");
        }
        if (merchantName == null || merchantName.isEmpty()) {
            throw new IllegalStateException("Merchant name is not configured");
        }

        logger.info("✅ Bakong payment service initialized for merchant: {}", merchantName);
    }

    /**
     * Generate Bakong KHQR code for payment
     */
    public BakongPaymentResponse generateQRCode(String billNumber, Double amount, String currency) {
        try {
            // =========================
            // 1. VALIDATION
            // =========================
            validateInputParameters(billNumber, amount, currency);

            String finalCurrency = (currency == null) ? "KHR" : currency;

            logger.info("✅ Generating REAL Bakong QR");
            logger.info("Bill: {}, Amount: {}, Currency: {}", billNumber, amount, finalCurrency);

            // =========================
            // 2. BUILD KHQR REQUEST
            // =========================
            IndividualInfo info = buildIndividualInfo(billNumber, amount, finalCurrency);

            // Set expiration timestamp (15 minutes from now)
            boolean expirationSet = setExpirationTimestamp(info, 15);

            if (!expirationSet) {
                logger.warn("⚠️ Could not set expiration timestamp - proceeding without it");
            }

            // =========================
            // 3. CALL BAKONG SDK
            // =========================
            KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(info);

            validateBakongResponse(response);

            // =========================
            // 4. BUILD RESPONSE
            // =========================
            BakongPaymentResponse paymentResponse = buildPaymentResponse(
                    response.getData(), billNumber, amount, finalCurrency
            );

            logger.info("✅ Bakong QR generated successfully");
            return paymentResponse;

        } catch (Exception e) {
            logger.error("❌ Error generating Bakong QR", e);
            throw new RuntimeException("Error generating Bakong QR: " + e.getMessage(), e);
        }
    }

    /**
     * Validate input parameters
     */
    private void validateInputParameters(String billNumber, Double amount, String currency) {
        if (billNumber == null || billNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bill number is required");
        }
        if (billNumber.length() > 25) {
            throw new IllegalArgumentException("Bill number cannot exceed 25 characters");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (currency != null && !currency.equals("USD") && !currency.equals("KHR")) {
            throw new IllegalArgumentException("Currency must be USD or KHR");
        }
    }

    /**
     * Build IndividualInfo object for Bakong SDK
     */
    private IndividualInfo buildIndividualInfo(String billNumber, Double amount, String currency) {
        IndividualInfo info = new IndividualInfo();

        info.setBakongAccountId(bakongAccountId);
        info.setCurrency("USD".equals(currency) ? KHQRCurrency.USD : KHQRCurrency.KHR);
        info.setAmount(amount);
        info.setMerchantName(merchantName);
        info.setMerchantCity(merchantCity);
        info.setBillNumber(billNumber);
        info.setStoreLabel(storeLabel);
        info.setTerminalLabel(terminalLabel);

        // Optional fields
        if (accountPhone != null && !accountPhone.isBlank()) {
            info.setAccountInformation(accountPhone);
            info.setMobileNumber(accountPhone);
        }
        if (acquiringBank != null && !acquiringBank.isBlank()) {
            info.setAcquiringBank(acquiringBank);
        }

        return info;
    }

    /**
     * Set expiration timestamp using reflection (tries multiple approaches)
     */
    private boolean setExpirationTimestamp(IndividualInfo info, int minutesFromNow) {
        try {
            // Calculate expiration timestamp (epoch milliseconds)
            LocalDateTime expirationTime = LocalDateTime.now(ZoneId.of("Asia/Phnom_Penh")).plusMinutes(minutesFromNow);
            Long expirationTimestamp = expirationTime.atZone(ZoneId.of("Asia/Phnom_Penh")).toInstant().toEpochMilli();

            logger.info("🔍 Attempting to set expiration timestamp: {} (epoch ms)", expirationTimestamp);

            // Approach 1: Try setter methods with Long parameter
            if (trySetterMethods(info, expirationTimestamp)) {
                return true;
            }

            // Approach 2: Try direct field access
            if (tryFieldAccess(info, expirationTimestamp)) {
                return true;
            }

            // Approach 3: Debug available methods and fields
            debugAvailableSettersAndFields(info);

            return false;

        } catch (Exception e) {
            logger.error("❌ Error setting expiration timestamp: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Try to set expiration using setter methods
     */
    private boolean trySetterMethods(IndividualInfo info, Long expirationTimestamp) {
        String[] expirationMethods = {
                "setExpirationTimestamp", "setExpireDate", "setExpiration",
                "setTimestamp", "setExpiredDate", "setExpiryDate",
                "setExpiryTimestamp", "setExpiry", "setValidUntil"
        };

        // Try with Long parameter first
        for (String methodName : expirationMethods) {
            try {
                Method method = info.getClass().getMethod(methodName, Long.class);
                method.invoke(info, expirationTimestamp);
                logger.info("✅ Set expiration using method: {} with Long", methodName);
                return true;
            } catch (NoSuchMethodException e) {
                // Try with String parameter as fallback
                try {
                    Method method = info.getClass().getMethod(methodName, String.class);
                    String timestampString = expirationTimestamp.toString();
                    method.invoke(info, timestampString);
                    logger.info("✅ Set expiration using method: {} with String", methodName);
                    return true;
                } catch (Exception e2) {
                    // Continue to next method
                }
            } catch (Exception e) {
                logger.debug("❌ Failed to invoke {}: {}", methodName, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Try to set expiration using direct field access
     */
    private boolean tryFieldAccess(IndividualInfo info, Long expirationTimestamp) {
        String[] fieldNames = {
                "expirationTimestamp", "expireDate", "expiration",
                "timestamp", "expiredDate", "expiryDate",
                "expiryTimestamp", "expiry", "validUntil"
        };

        for (String fieldName : fieldNames) {
            try {
                Field field = info.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                // Check field type and set accordingly
                Class<?> fieldType = field.getType();
                if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                    field.set(info, expirationTimestamp);
                } else if (fieldType.equals(String.class)) {
                    field.set(info, expirationTimestamp.toString());
                } else {
                    logger.warn("⚠️ Field {} has unsupported type: {}", fieldName, fieldType);
                    continue;
                }

                logger.info("✅ Set expiration via field: {} (type: {})", fieldName, fieldType.getSimpleName());
                return true;
            } catch (NoSuchFieldException e) {
                // Continue to next field
            } catch (Exception e) {
                logger.error("❌ Failed to set field {}: {}", fieldName, e.getMessage());
            }
        }
        return false;
    }

    /**
     * Debug available setter methods and fields
     */
    private void debugAvailableSettersAndFields(IndividualInfo info) {
        logger.info("🔍 Available setter methods:");
        for (Method method : info.getClass().getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                logger.info("  - {} ({})", method.getName(), method.getParameterTypes()[0].getSimpleName());
            }
        }

        logger.info("🔍 Available fields:");
        for (Field field : info.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            logger.info("  - {} ({})", field.getName(), field.getType().getSimpleName());
        }
    }

    /**
     * Validate Bakong SDK response
     */
    private void validateBakongResponse(KHQRResponse<KHQRData> response) {
        if (response == null || response.getKHQRStatus() == null) {
            throw new RuntimeException("Bakong SDK returned null response");
        }

        if (response.getKHQRStatus().getCode() != 0) {
            String errorMessage = response.getKHQRStatus().getMessage();
            logger.error("❌ Bakong KHQR generation failed: {}", errorMessage);
            throw new RuntimeException("Bakong KHQR failed: " + errorMessage);
        }

        if (response.getData() == null) {
            throw new RuntimeException("Bakong SDK returned null data");
        }
    }

    /**
     * Build payment response DTO
     */
    private BakongPaymentResponse buildPaymentResponse(KHQRData data, String billNumber, Double amount, String currency) {
        BakongPaymentResponse response = new BakongPaymentResponse();
        response.setQrCode(data.getQr());
        response.setMd5(data.getMd5());
        response.setBillNumber(billNumber);
        response.setAmount(amount);
        response.setCurrency(currency);
        return response;
    }
}
