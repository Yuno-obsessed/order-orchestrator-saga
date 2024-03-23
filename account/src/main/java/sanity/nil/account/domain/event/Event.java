package sanity.nil.account.domain.event;

import sanity.nil.account.domain.consts.EventStatus;

import java.util.UUID;

public interface Event {

    UUID getEntityID();

    String getEntityType();

    EventStatus getStatus();

    String getType();

    Object getPayload();
}
