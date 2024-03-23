package sanity.nil.order.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanity.nil.order.application.OrderCommands;
import sanity.nil.order.application.OrderQueries;
import sanity.nil.order.application.dto.CreatedOrderDTO;
import sanity.nil.order.application.dto.GetOrderByTraceDTO;
import sanity.nil.order.application.dto.OrderQueryDTO;
import sanity.nil.order.application.dto.StartCreateOrderCommandDTO;

import java.util.UUID;

@RestController
@RequestMapping(value = "order")
@RequiredArgsConstructor
public class RestApi {

    private final OrderCommands orderCommands;
    private final OrderQueries orderQueries;

    @PostMapping
    public ResponseEntity<CreatedOrderDTO> createOrder(@RequestBody StartCreateOrderCommandDTO dto) {
        return ResponseEntity
                .status(201)
                .body(orderCommands.startCreateOrderCommand.handle(dto));
    }

    @GetMapping("/{trace_id}")
    public ResponseEntity<OrderQueryDTO> getOrderByTraceID(@PathVariable("trace_id") String traceID) {
        GetOrderByTraceDTO dto = new GetOrderByTraceDTO(UUID.fromString(traceID));
        return ResponseEntity
                .status(200)
                .body(orderQueries.getOrderByTraceID.handle(dto));
    }

}
