package sanity.nil.account.application.interfaces;

import sanity.nil.account.application.dto.EventMessage;
import sanity.nil.account.domain.event.Event;

import java.util.Collection;

public interface OutboxRepository {

    void saveMessage(EventMessage event);

    void saveMessages(Collection<Event> event);

    Collection<Event> getNotProcessed();
}
