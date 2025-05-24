package com.example.sbt.module.payment;

import com.example.sbt.common.constant.CommonStatus;
import com.example.sbt.common.exception.PaymentException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.service.OrderService;
import com.example.sbt.module.payment.dto.PaymentDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class PaymentServiceImpl implements PaymentService {

    private final CommonMapper commonMapper;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final ExecutePaymentService executePaymentService;

    @Override
    public PaymentDTO createSession(UUID orderId, UUID userId) throws PaymentException {
        OrderDTO orderDTO = orderService.findOneByIdOrThrow(orderId, userId);
        String checkoutUrl = executePaymentService.createSessionUrl(orderDTO);
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setStatus(CommonStatus.PROCESSING);
        PaymentDTO result = commonMapper.toDTO(paymentRepository.save(payment));
        result.setCheckoutUrl(checkoutUrl);
        return result;
    }

    @Override
    public PaymentDTO handlePayment(UUID orderId) {
        return null;
    }

}
