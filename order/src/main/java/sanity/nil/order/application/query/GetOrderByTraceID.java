package sanity.nil.order.application.query;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.dto.GetOrderByTraceDTO;
import sanity.nil.order.application.dto.OrderQueryDTO;
import sanity.nil.order.application.interfaces.OrderRepository;

@RequiredArgsConstructor
public class GetOrderByTraceID {

    private final OrderRepository orderRepository;

    public OrderQueryDTO handle(GetOrderByTraceDTO dto) {
        return orderRepository.getByTraceID(dto.traceID());
    }
}
