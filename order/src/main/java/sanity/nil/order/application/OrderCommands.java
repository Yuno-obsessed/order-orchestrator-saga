package sanity.nil.order.application;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.command.CreateOrderCommand;
import sanity.nil.order.application.command.RejectOrderCommand;
import sanity.nil.order.application.command.StartCreateOrderCommand;

@RequiredArgsConstructor
public class OrderCommands {

    public final StartCreateOrderCommand startCreateOrderCommand;
    public final RejectOrderCommand rejectOrderCommand;
    public final CreateOrderCommand createOrderCommand;
}
