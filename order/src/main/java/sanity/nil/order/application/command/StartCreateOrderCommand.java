package sanity.nil.order.application.command;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.dto.CreatedOrderDTO;
import sanity.nil.order.application.dto.StartCreateOrderCommandDTO;
import sanity.nil.order.application.interfaces.OrderRepository;
import sanity.nil.order.application.interfaces.OutboxRepository;
import sanity.nil.order.domain.entity.Order;
import sanity.nil.order.domain.event.Event;

import java.util.List;

@RequiredArgsConstructor
public class StartCreateOrderCommand {

    private final OutboxRepository outboxRepository;
    private final OrderRepository orderRepository;

    public CreatedOrderDTO handle(StartCreateOrderCommandDTO dto) {
        Order order = new Order(dto.userID(), dto.amount());
        List<Event> events = order.getEvents();
        outboxRepository.saveMessages(events);
        orderRepository.save(order);
        return new CreatedOrderDTO(order.getTraceID());
    }
}
