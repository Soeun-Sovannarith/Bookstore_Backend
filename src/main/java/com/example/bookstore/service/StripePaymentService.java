package com.example.bookstore.service;

import com.example.bookstore.dto.StripePaymentRequest;
import com.example.bookstore.dto.StripePaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    /**
     * Create a Stripe Payment Intent
     *
     * @param request Payment request details
     * @return StripePaymentResponse with client secret
     * @throws StripeException if payment creation fails
     */
    public StripePaymentResponse createPaymentIntent(StripePaymentRequest request) throws StripeException {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount()) // Amount in cents
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "usd")
                .setDescription(request.getDescription())
                .setReceiptEmail(request.getReceiptEmail())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            StripePaymentResponse response = new StripePaymentResponse();
            response.setPaymentIntentId(paymentIntent.getId());
            response.setClientSecret(paymentIntent.getClientSecret());
            response.setStatus(paymentIntent.getStatus());
            response.setAmount(paymentIntent.getAmount());
            response.setCurrency(paymentIntent.getCurrency());

            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve a Payment Intent by ID
     *
     * @param paymentIntentId The Payment Intent ID
     * @return PaymentIntent object
     * @throws StripeException if retrieval fails
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

    /**
     * Confirm a Payment Intent
     *
     * @param paymentIntentId The Payment Intent ID to confirm
     * @return Confirmed PaymentIntent
     * @throws StripeException if confirmation fails
     */
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.confirm();
    }

    /**
     * Cancel a Payment Intent
     *
     * @param paymentIntentId The Payment Intent ID to cancel
     * @return Cancelled PaymentIntent
     * @throws StripeException if cancellation fails
     */
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.cancel();
    }
}
