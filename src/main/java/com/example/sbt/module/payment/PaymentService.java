package com.example.sbt.module.payment;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.payment.dto.PaymentDTO;
import com.example.sbt.module.payment.dto.SearchPaymentRequestDTO;

import java.util.UUID;

public interface PaymentService {

    PaymentDTO save(PaymentDTO requestDTO);

    PaymentDTO findOneById(UUID id);

    PaymentDTO findOneByIdOrThrow(UUID id);

    PaginationData<PaymentDTO> search(SearchPaymentRequestDTO requestDTO, boolean isCount);

}
