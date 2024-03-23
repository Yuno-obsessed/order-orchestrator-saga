package sanity.nil.account.application.dto;

import sanity.nil.account.infrastructure.broker.dto.VerifyCustomerEvent;

import java.util.UUID;

public record VerifyBalanceCommandDTO(
        VerifyCustomerEvent.Payload verifyCustomerEventPayload,
        UUID sagaID
) {}
