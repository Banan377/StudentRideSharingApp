package com.example.rideapp.controller;

import com.example.rideapp.models.PaymentModel;
import com.example.rideapp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Get all payments
    @GetMapping
    public List<PaymentModel> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentModel> getPaymentById(@PathVariable Long id) {
        Optional<PaymentModel> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Create new payment
    @PostMapping
    public PaymentModel createPayment(@RequestBody PaymentModel payment) {
        return paymentService.createPayment(payment);
    }

    // Update payment status
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentModel> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            PaymentModel updatedPayment = paymentService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(updatedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get payments by user
    @GetMapping("/user/{userId}")
    public List<PaymentModel> getPaymentsByUser(@PathVariable Long userId) {
        return paymentService.getPaymentsByUser(userId);
    }

    // Process payment (simulate payment processing)
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentModel> processPayment(@PathVariable Long id) {
        try {
            PaymentModel processedPayment = paymentService.processPayment(id);
            return ResponseEntity.ok(processedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}