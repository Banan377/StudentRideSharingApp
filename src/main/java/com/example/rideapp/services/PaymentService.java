package com.example.rideapp.services;

import com.example.rideapp.models.PaymentModel;
import com.example.rideapp.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    public PaymentModel processPayment(PaymentModel payment) {
        // محاكاة عملية الدفع
        payment.setStatus("COMPLETED");
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        return paymentRepository.save(payment);
    }
    
    public List<PaymentModel> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public PaymentModel getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
    
    public List<PaymentModel> getPaymentsByRideId(Long rideId) {
        return paymentRepository.findByRideId(rideId);
    }
    
    public List<PaymentModel> getPaymentsByPassengerId(Long passengerId) {
        return paymentRepository.findByPassengerId(passengerId);
    }
    
    public PaymentModel updatePaymentStatus(Long id, String status) {
        PaymentModel payment = paymentRepository.findById(id).orElse(null);
        if (payment != null) {
            payment.setStatus(status);
            return paymentRepository.save(payment);
        }
        return null;
    }
}