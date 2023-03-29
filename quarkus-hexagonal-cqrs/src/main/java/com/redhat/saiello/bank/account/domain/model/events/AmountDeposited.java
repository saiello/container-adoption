package com.redhat.saiello.bank.account.domain.model.events;

import lombok.ToString;

@ToString
public class AmountDeposited extends BankAccountEvent<AmountDeposited.Payload> {

    public static final String TYPE = "AmountDeposited";

    public AmountDeposited(String aggregateId, AmountDeposited.Payload payload) {
        super(aggregateId, TYPE, payload);
    }

    public static record Payload(Double amount){}
}
