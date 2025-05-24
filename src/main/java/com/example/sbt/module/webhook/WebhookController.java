package com.example.sbt.module.webhook;

import com.example.sbt.common.constant.ApplicationProperties;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.module.payment.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/webhooks")
public class WebhookController {

    private final ApplicationProperties applicationProperties;
    private final PaymentService paymentService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ) {
        Event event;
        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    applicationProperties.getStripeWebhookSecret()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
            paymentService.handlePayment(ConversionUtils.toUUID(session.getClientReferenceId()));
        }

        return ResponseEntity.ok("Webhook processed");
    }

}
