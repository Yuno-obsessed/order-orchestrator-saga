package sanity.nil.order.infrastructure.saga.consts;

public enum SagaStepStatus {
    STARTED,
    FAILED,
    SUCCEEDED,
    COMPENSATING,
    COMPENSATED;
}
