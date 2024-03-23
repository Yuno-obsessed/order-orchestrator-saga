package sanity.nil.order.application.interfaces;

import sanity.nil.order.application.dto.EventMessage;
import sanity.nil.order.domain.event.Event;

import java.util.Collection;

public interface OutboxRepository {

    void saveMessage(EventMessage event);

    void saveMessages(Collection<Event> event);

    Collection<Event> getNotProcessed();
}
