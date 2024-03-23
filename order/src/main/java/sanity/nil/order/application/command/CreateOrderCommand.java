package sanity.nil.order.application.command;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.dto.CreateOrderCommandDTO;
import sanity.nil.order.application.interfaces.OrderRepository;
import sanity.nil.order.domain.consts.OrderStatus;

@RequiredArgsConstructor
public class CreateOrderCommand {

    private final OrderRepository orderRepository;

    public void handle(CreateOrderCommandDTO dto) {
        orderRepository.changeStatus(dto.orderID(), dto.userID(), OrderStatus.CREATED);
    }
}
