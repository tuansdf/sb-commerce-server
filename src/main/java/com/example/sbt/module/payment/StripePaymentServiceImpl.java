package com.example.sbt.module.payment;

import com.example.sbt.common.constant.ApplicationProperties;
import com.example.sbt.common.exception.PaymentException;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.module.order.dto.OrderDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class StripePaymentServiceImpl implements ExecutePaymentService {

    private final ApplicationProperties applicationProperties;

    @Override
    public String createSessionUrl(OrderDTO requestDTO) throws PaymentException {
        try {
            List<SessionCreateParams.LineItem> lineItems = requestDTO.getItems().stream()
                    .map(item -> SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(ConversionUtils.safeToLong(item.getUnitPrice() * 100)) // in cents
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(item.getProductName())
                                            .build())
                                    .build())
                            .setQuantity((long) item.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(applicationProperties.getStripeSuccessUtl() + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(applicationProperties.getStripeCancelUrl() + "?order_id=" + requestDTO.getId())
                    .addAllLineItem(lineItems)
                    .setClientReferenceId(requestDTO.getId().toString())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new PaymentException(e);
        }
    }
    
    @Override
    private void handleSuccessfulPayment(Session session) {
        String orderId = session.getClientReferenceId();
        String paymentIntentId = session.getPaymentIntent();
        session.getAmountSubtotal();
        // Update your order status
        orderService.completeOrder(
                Long.parseLong(orderId),
                paymentIntentId,
                "stripe"
        );
    }

}
