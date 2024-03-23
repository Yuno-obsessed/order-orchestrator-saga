package sanity.nil.account.presentation.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sanity.nil.account.application.AccountCommands;
import sanity.nil.account.application.dto.VerifyBalanceCommandDTO;
import sanity.nil.account.infrastructure.broker.dto.VerifyCustomerEvent;
import sanity.nil.account.infrastructure.broker.impl.RequiredHeaderEmptyException;

import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountConsumer {

    private final ObjectMapper objectMapper;
    private final AccountCommands accountCommands;

    @KafkaListener(topics = "${application.broker.order-topic}", groupId = "1")
    public void orderEntityEvents(@Payload ConsumerRecord<String, String> message) {

        String eventType = Arrays.stream(message.headers().toArray())
                .filter(h -> h.key().equals("event_type"))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElseThrow(() -> new RequiredHeaderEmptyException("event_type"));

        String sagaHeaderValue = Arrays.stream(message.headers().toArray())
                .filter(h -> h.key().equals("saga_id"))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElseThrow(() -> new RequiredHeaderEmptyException("saga_id"));

        UUID sagaID = UUID.fromString(sagaHeaderValue);
        String eventPayload = message.value();

        try {
            if (eventType.equals("VerifyCustomer")) {
                VerifyCustomerEvent.Payload verifyCustomerEventPayload =
                        objectMapper.readValue(eventPayload, VerifyCustomerEvent.Payload.class);
                accountCommands.verifyBalanceCommand.handle(new VerifyBalanceCommandDTO(verifyCustomerEventPayload, sagaID));
            }
            log.info(message.value());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return;
        }
    }

}
