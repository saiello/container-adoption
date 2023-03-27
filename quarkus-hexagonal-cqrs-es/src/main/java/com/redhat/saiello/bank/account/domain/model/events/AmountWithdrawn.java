package com.redhat.saiello.bank.account.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class AmountWithdrawn extends BankAccountEvent {

    public static final String TYPE = "AmountWithdrawn";
    private final Double amount;

    @JsonCreator
    public AmountWithdrawn(@JsonProperty("amount") Double amount) {
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
