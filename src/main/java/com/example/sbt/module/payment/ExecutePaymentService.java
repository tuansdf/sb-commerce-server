package com.example.sbt.module.payment;

import com.example.sbt.common.exception.PaymentException;
import com.example.sbt.module.order.dto.OrderDTO;

public interface ExecutePaymentService {

    String createSessionUrl(OrderDTO requestDTO) throws PaymentException;

}
