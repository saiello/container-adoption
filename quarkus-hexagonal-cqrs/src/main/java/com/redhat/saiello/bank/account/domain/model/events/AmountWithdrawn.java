package com.redhat.saiello.bank.account.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import io.debezium.outbox.quarkus.ExportedEvent;
import lombok.ToString;

@ToString
public class AmountWithdrawn extends BankAccountEvent<AmountWithdrawn.Payload> implements ExportedEvent<String, JsonNode> {

    public static final String TYPE = "AmountWithdrawn";

    public AmountWithdrawn(String aggregateId, Payload payload) {
        super(aggregateId, TYPE, payload);
    }

    public static record Payload(Double amount){}
}
