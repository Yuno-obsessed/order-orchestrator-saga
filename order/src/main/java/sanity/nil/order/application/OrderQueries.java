package sanity.nil.order.application;

import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.query.GetOrderByTraceID;

@RequiredArgsConstructor
public class OrderQueries {
    public final GetOrderByTraceID getOrderByTraceID;
}
