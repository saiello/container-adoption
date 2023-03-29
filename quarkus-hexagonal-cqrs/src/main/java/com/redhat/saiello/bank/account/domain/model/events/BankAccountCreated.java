package com.redhat.saiello.bank.account.domain.model.events;

import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import lombok.ToString;

@ToString
public class BankAccountCreated extends BankAccountEvent<BankAccountCreated.Payload> {
    public static final String TYPE = "AccountCreated";

    public BankAccountCreated(String aggregateId, Payload payload) {
        super(aggregateId, TYPE, payload);
    }

    public static record Payload(PersonalInfo personalInfo){}

}
