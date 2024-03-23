package sanity.nil.order.domain.entity;

import sanity.nil.order.domain.event.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityBase {

    private Collection<Event> events;

    public EntityBase() {
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        List<Event> eventCollection = new ArrayList<>(events);
        events.clear();
        return eventCollection;
    }
}
