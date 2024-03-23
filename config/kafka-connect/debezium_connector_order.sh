#!/bin/bash

curl -i -X PUT -H "Content-Type: application/json" \
    http://localhost:8083/connectors/debezium-order-outbox/config \
    -d '{
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "tasks.max": "1",
        "plugin.name": "pgoutput",
        "database.hostname": "order-db",
        "database.port": "5432",
        "database.user": "sanity",
        "database.password": "wordpass",
        "database.dbname": "order_db",
        "database.server.name": "dbserver1",
        "topic.prefix": "order_service",
        "schema.include.list": "public",
        "table.include.list": "public.outbox",
        "tombstones.on.delete": "false",
        "include.schema.changes": "false",
        "errors.deadletterqueue.topic.name": "debezium-dlq",
        "transforms": "outbox",
        "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
        "transforms.outbox.table.fields.additional.placement": "type:header:event_type,saga_id:header:saga_id",
        "transforms.outbox.table.fields.additional.error.on.missing": "false",
        "transforms.outbox.route.by.field": "entity_type",
        "transforms.outbox.route.topic.replacement": "outbox.event.${routedByValue}",
        "transforms.outbox.table.field.event.key": "entity_id",
        "transforms.outbox.table.field.event.id": "id",
        "transforms.outbox.table.field.event.type": "type",
        "transforms.outbox.table.expand.json.payload": "true",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "key.converter": "org.apache.kafka.connect.json.JsonConverter",
        "key.converter.schemas.enable": "false",
        "value.converter.schemas.enable": "false",
        "transforms.outbox.table.field.event.timestamp": "created_at",
        "transforms.outbox.table.field.event.payload": "payload"
    }'
#        "transforms.filter.condition": "value.source.table == 'table-name' && (value.op == 'd' || value.op == 'c' || (value.op == 'u' && value.after.status != value.before.status))"

sleep 2

curl -s 'http://localhost:8083/connector-plugins' | jq '.'

curl -s 'http://localhost:8083/connectors/debezium-order-outbox/config' | jq '.'
