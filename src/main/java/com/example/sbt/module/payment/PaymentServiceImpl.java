package com.example.sbt.module.payment;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.payment.dto.PaymentDTO;
import com.example.sbt.module.payment.dto.SearchPaymentRequestDTO;
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

    @Override
    public PaymentDTO save(PaymentDTO requestDTO) {
        return null;
    }

    @Override
    public PaymentDTO findOneById(UUID id) {
        return null;
    }

    @Override
    public PaymentDTO findOneByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public PaginationData<PaymentDTO> search(SearchPaymentRequestDTO requestDTO, boolean isCount) {
        return null;
    }

}
