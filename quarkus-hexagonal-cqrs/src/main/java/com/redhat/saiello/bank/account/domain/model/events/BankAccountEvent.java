package com.redhat.saiello.bank.account.domain.model.events;

import com.fasterxml.jackson.databind.JsonNode;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

import static com.redhat.saiello.common.JacksonSerDe.serialize;

abstract class BankAccountEvent<T> implements ExportedEvent<String, JsonNode> {

    private final String aggregateId;
    private final Instant timestamp;
    private final String type;
    private final T payload;

    public BankAccountEvent(String aggregateId, String type, T payload) {
        this.aggregateId = aggregateId;
        this.timestamp = Instant.now();
        this.type = type;
        this.payload = payload;
    }

    @Override
    public String getAggregateId() {
        return this.aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "BankAccount";
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Override
    public JsonNode getPayload() {
        return serialize(payload);
    }
}
