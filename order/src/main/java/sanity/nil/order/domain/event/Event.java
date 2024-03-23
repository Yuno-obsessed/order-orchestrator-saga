package sanity.nil.order.domain.event;

import sanity.nil.order.domain.consts.EventStatus;

import java.util.UUID;

public interface Event {

    UUID getEntityID();

    String getEntityType();

    EventStatus getStatus();

    String getType();

    Object getPayload();
}
