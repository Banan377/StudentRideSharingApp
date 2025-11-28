package com.example.rideapp.service;

import com.example.rideapp.models.PaymentModel;
import com.example.rideapp.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentModel> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<PaymentModel> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public PaymentModel createPayment(PaymentModel payment) {
        payment.setPaymentDate(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public PaymentModel updatePaymentStatus(Long id, String status) {
        PaymentModel payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        try {
            PaymentModel.PaymentStatus newStatus = PaymentModel.PaymentStatus.valueOf(status.toUpperCase());
            payment.setPaymentStatus(newStatus);
            return paymentRepository.save(payment);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment status");
        }
    }

    public List<PaymentModel> getPaymentsByUser(Long userId) {
        return paymentRepository.findByPassengerIdOrDriverId(userId, userId);
    }

    public PaymentModel processPayment(Long id) {
        PaymentModel payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Simulate payment processing
        if (payment.getPaymentStatus() == PaymentModel.PaymentStatus.PENDING) {
            payment.setPaymentStatus(PaymentModel.PaymentStatus.COMPLETED);
            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Payment already processed");
        }
    }
}