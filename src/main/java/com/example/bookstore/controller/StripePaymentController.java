package com.example.bookstore.controller;

import com.example.bookstore.dto.StripePaymentRequest;
import com.example.bookstore.dto.StripePaymentResponse;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.Payment;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.service.PaymentService;
import com.example.bookstore.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/stripe")
@CrossOrigin(origins = "http://localhost:3000")
public class StripePaymentController {

    private final StripePaymentService stripePaymentService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public StripePaymentController(StripePaymentService stripePaymentService,
                                   OrderService orderService,
                                   PaymentService paymentService) {
        this.stripePaymentService = stripePaymentService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    /**
     * Get Stripe publishable key for frontend
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publishableKey", stripePublicKey);
        return ResponseEntity.ok(config);
    }

    /**
     * Create a payment intent for an order
     *
     * POST /api/payments/stripe/create-payment-intent
     * Body: {
     *   "orderId": 1,
     *   "currency": "usd",
     *   "receiptEmail": "customer@example.com"
     * }
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody StripePaymentRequest request) {
        try {
            // Validate order exists
            Order order = orderService.getOrderById(request.getOrderId().intValue());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
            }

            // Convert amount to cents (Stripe requires amount in smallest currency unit)
            Long amountInCents = (long) (order.getTotalAmount() * 100);
            request.setAmount(amountInCents);

            // Set description
            if (request.getDescription() == null) {
                request.setDescription("Order #" + order.getId() + " - BookHaven");
            }

            // Create payment intent via Stripe
            StripePaymentResponse response = stripePaymentService.createPaymentIntent(request);

            // Save payment record in database
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentMethod("STRIPE");
            payment.setPaymentStatus("PENDING");
            payment.setCreatedAt(new Date());
            paymentService.createPayment(payment);

            // Add payment ID to response
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("clientSecret", response.getClientSecret());
            responseMap.put("paymentIntentId", response.getPaymentIntentId());
            responseMap.put("amount", response.getAmount());
            responseMap.put("currency", response.getCurrency());
            responseMap.put("status", response.getStatus());
            responseMap.put("paymentId", payment.getId());

            return ResponseEntity.ok(responseMap);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create payment: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error processing payment: " + e.getMessage()));
        }
    }

    /**
     * Retrieve payment intent status
     *
     * GET /api/payments/stripe/payment-intent/{paymentIntentId}
     */
    @GetMapping("/payment-intent/{paymentIntentId}")
    public ResponseEntity<?> getPaymentIntent(@PathVariable String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripePaymentService.retrievePaymentIntent(paymentIntentId);

            Map<String, Object> response = new HashMap<>();
            response.put("id", paymentIntent.getId());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());
            response.put("status", paymentIntent.getStatus());
            response.put("created", paymentIntent.getCreated());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve payment: " + e.getMessage()));
        }
    }

    /**
     * Confirm payment and update order status
     *
     * POST /api/payments/stripe/confirm
     * Body: {
     *   "paymentIntentId": "pi_xxx",
     *   "orderId": 1
     * }
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> request) {
        try {
            String paymentIntentId = (String) request.get("paymentIntentId");
            Integer orderId = Integer.parseInt(request.get("orderId").toString());

            // Retrieve payment intent from Stripe
            PaymentIntent paymentIntent = stripePaymentService.retrievePaymentIntent(paymentIntentId);

            // Update payment status in database
            Payment payment = paymentService.getPaymentsByOrderId(orderId).stream()
                .filter(p -> "STRIPE".equals(p.getPaymentMethod()))
                .findFirst()
                .orElse(null);

            if (payment != null) {
                if ("succeeded".equals(paymentIntent.getStatus())) {
                    payment.setPaymentStatus("COMPLETED");

                    // Update order status
                    Order order = orderService.getOrderById(orderId);
                    order.setStatus("PAID");
                    orderService.updateOrder(orderId, order);
                } else {
                    payment.setPaymentStatus(paymentIntent.getStatus().toUpperCase());
                }
                paymentService.updatePayment(payment.getId(), payment);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", paymentIntent.getStatus());
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("orderId", orderId);

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to confirm payment: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error confirming payment: " + e.getMessage()));
        }
    }

    /**
     * Cancel a payment intent
     *
     * POST /api/payments/stripe/cancel/{paymentIntentId}
     */
    @PostMapping("/cancel/{paymentIntentId}")
    public ResponseEntity<?> cancelPayment(@PathVariable String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripePaymentService.cancelPaymentIntent(paymentIntentId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", paymentIntent.getStatus());
            response.put("paymentIntentId", paymentIntent.getId());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to cancel payment: " + e.getMessage()));
        }
    }

    /**
     * Webhook endpoint for Stripe events
     * This endpoint receives real-time updates from Stripe about payment events
     *
     * POST /api/payments/stripe/webhook
     */
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload,
                                          @RequestHeader("Stripe-Signature") String sigHeader) {
        // TODO: Implement webhook signature verification
        // For now, just acknowledge receipt
        return ResponseEntity.ok().build();
    }
}
