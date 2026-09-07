package com.zestmarket.payment.controller;

import com.zestmarket.payment.dto.*;
import com.zestmarket.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Gateway APIs", description = "Razorpay Sandbox Order Creation & Payment Verification")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    @Operation(summary = "Create Razorpay payment order")
    public ResponseEntity<PaymentResponse> createPaymentOrder(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentOrder(request));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify Razorpay payment signature & update payment status")
    public ResponseEntity<PaymentResponse> verifyPayment(@Valid @RequestBody PaymentVerificationRequest request) {
        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }
}
