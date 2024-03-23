CREATE TABLE public.outbox
(
    created_at  timestamp(6),
    entity_id   uuid,
    id          uuid not null,
    entity_type varchar(255),
    saga_id     varchar(255),
    type        varchar(255),
    payload     jsonb,
    primary key (id)
);

CREATE TABLE public.orders
(
    amount       numeric(38, 2),
    created_at   timestamp(6),
    id           uuid not null,
    trace_id     uuid,
    user_id      uuid,
    order_status varchar(255) check (order_status in ('CREATE_PENDING', 'SHIPPED', 'DELIVERED', 'CREATED', 'CANCELED')),
    primary key (id)
);

CREATE TABLE public.saga_state
(
    created_at   timestamp(6),
    updated_at   timestamp(6),
    id           uuid not null,
    saga_status  varchar(255) check (saga_status in ('STARTED', 'ABORTING', 'ABORTED', 'COMPLETED')),
    saga_type    varchar(255),
    current_step jsonb,
    payload      jsonb,
    primary key (id)
);