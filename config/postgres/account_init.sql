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

CREATE TABLE public.accounts
(
    active     boolean,
    balance    numeric(38, 2),
    created_at timestamptz,
    id         uuid not null,
    username   varchar(255),
    primary key (id)
);

INSERT INTO public.accounts(active, balance, created_at, id, username) VALUES (true, 12000.25, now(), 'e9e13170-50a3-4757-9ca3-14bf8452e7be', 'username');
