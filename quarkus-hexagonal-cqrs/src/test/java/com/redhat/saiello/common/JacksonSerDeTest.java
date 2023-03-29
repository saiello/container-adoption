package com.redhat.saiello.common;

import com.redhat.saiello.bank.account.domain.model.BankAccount;
import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import io.debezium.outbox.quarkus.internal.JsonNodeAttributeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonSerDeTest {

    @Test void deserialize(){
        BankAccountCreated.Payload deserialize = JacksonSerDe.deserialize("{\"personalInfo\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}", BankAccountCreated.Payload.class);
        BankAccountCreated.Payload foo = JacksonSerDe.deserialize("""
                {\\"personalInfo\\":{\\"firstName\\":\\"John\\",\\"lastName\\":\\"Doe\\"}}
                """.replaceAll("\\\\", ""), BankAccountCreated.Payload.class);
    }

    @Test void serialize(){
        var payload = new BankAccountCreated.Payload(new PersonalInfo("John", "Doe"));
        var jsonNode = JacksonSerDe.serialize(payload);
        var stringVal = new JsonNodeAttributeConverter().convertToDatabaseColumn(jsonNode);

        JacksonSerDe.deserialize(stringVal, BankAccountCreated.Payload.class);
    }
}