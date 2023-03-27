package com.redhat.saiello.bank.account.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class AmountDeposited extends BankAccountEvent {

    public static final String TYPE = "AmountDeposited";

    private final Double amount;

    @JsonCreator
    public AmountDeposited(@JsonProperty("amount") Double amount) {
        this.amount = amount;
    }

    @Override
    public String type() {
        return TYPE;
    }

    public Double getAmount(){
        return amount;
    }
}
