package sanity.nil.order.application.command;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.dto.RejectOrderCommandDTO;
import sanity.nil.order.application.interfaces.OrderRepository;
import sanity.nil.order.domain.consts.OrderStatus;

@RequiredArgsConstructor
public class RejectOrderCommand {

    private final OrderRepository orderRepository;

    public void handle(RejectOrderCommandDTO dto) {
        orderRepository.changeStatus(dto.orderID(), dto.userID(), OrderStatus.CANCELED);
    }
}
