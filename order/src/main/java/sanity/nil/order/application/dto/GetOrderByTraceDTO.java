package sanity.nil.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GetOrderByTraceDTO (
        @JsonProperty(value = "trace_id") UUID traceID
){}
