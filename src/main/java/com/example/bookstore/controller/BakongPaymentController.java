package com.example.bookstore.controller;

import com.example.bookstore.dto.BakongPaymentRequest;
import com.example.bookstore.dto.BakongPaymentResponse;
import com.example.bookstore.service.BakongPaymentService;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payments/bakong")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class BakongPaymentController {

    private static final Logger logger = LoggerFactory.getLogger(BakongPaymentController.class);

    @Autowired
    private BakongPaymentService bakongPaymentService;

    @Autowired
    private OrderService orderService;

    /**
     * Generate Bakong QR code for an order
     *
     * @param request Payment request containing orderId, amount, and currency
     * @return BakongPaymentResponse with QR code data
     */
    @PostMapping("/generate-qr")
    public ResponseEntity<?> generateQRCode(@RequestBody BakongPaymentRequest request) {
        try {
            logger.info("Received Bakong QR generation request for orderId: {}", request.getOrderId());

            // Validate order exists - convert Long to Integer
            Order order = orderService.getOrderById(request.getOrderId().intValue());

            if (order == null) {
                logger.warn("Order not found with ID: {}", request.getOrderId());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Order not found");
                return ResponseEntity.status(404).body(error);
            }

            logger.info("Order found: ID={}, Amount={}", order.getId(), order.getTotalAmount());

            // Generate unique bill number
            String billNumber = "ORDER-" + request.getOrderId() + "-" + System.currentTimeMillis();

            // Determine currency (default to USD if not specified)
            String currency = request.getCurrency() != null ? request.getCurrency() : "USD";

            logger.info("Generating Bakong QR code with currency: {}, amount: {}", currency, order.getTotalAmount());

            // Generate QR code - convert float to Double
            BakongPaymentResponse response = bakongPaymentService.generateQRCode(
                billNumber,
                Double.valueOf(order.getTotalAmount()),
                currency
            );

            logger.info("Bakong QR code generated successfully for order: {}", request.getOrderId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error generating Bakong QR code for orderId: {}", request.getOrderId(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to generate QR code: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Verify payment status
     *
     * @param md5 MD5 hash from QR generation
     * @param orderId Order ID to verify
     * @return Payment status
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
        @RequestParam String md5,
        @RequestParam Long orderId
    ) {
        // TODO: Implement payment verification logic
        // This would check if payment was received via:
        // 1. Webhook from Bakong
        // 2. Polling Bakong API
        // 3. Database record update

        return ResponseEntity.ok().body(Map.of(
            "status", "pending",
            "message", "Payment verification not yet implemented"
        ));
    }

    /**
     * Webhook endpoint for Bakong payment notifications
     * (Optional - requires Bakong webhook setup)
     */
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        // TODO: Implement webhook handler
        // 1. Verify webhook signature
        // 2. Extract payment details
        // 3. Update order status
        // 4. Send confirmation to customer

        return ResponseEntity.ok().build();
    }
}
