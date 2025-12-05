package com.example.rideapp.controller;

import com.example.rideapp.models.PaymentModel;
import com.example.rideapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/process")
    public ResponseEntity<PaymentModel> processPayment(@RequestBody PaymentModel payment) {
        PaymentModel processedPayment = paymentService.processPayment(payment);
        return ResponseEntity.ok(processedPayment);
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentModel>> getAllPayments() {
        List<PaymentModel> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentModel> getPaymentById(@PathVariable Long id) {
        PaymentModel payment = paymentService.getPaymentById(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<List<PaymentModel>> getPaymentsByRideId(@PathVariable Long rideId) {
        List<PaymentModel> payments = paymentService.getPaymentsByRideId(rideId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<PaymentModel>> getPaymentsByPassengerId(@PathVariable Long passengerId) {
        List<PaymentModel> payments = paymentService.getPaymentsByPassengerId(passengerId);
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentModel> updatePaymentStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        PaymentModel updatedPayment = paymentService.updatePaymentStatus(id, status);
        return updatedPayment != null ? 
                ResponseEntity.ok(updatedPayment) : 
                ResponseEntity.notFound().build();
    }
}