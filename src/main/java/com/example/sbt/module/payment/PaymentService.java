package com.example.sbt.module.payment;

import com.example.sbt.common.exception.PaymentException;
import com.example.sbt.module.payment.dto.PaymentDTO;

import java.util.UUID;

public interface PaymentService {

    PaymentDTO createSession(UUID orderId, UUID userId) throws PaymentException;

    PaymentDTO handlePayment(UUID orderId);
}
