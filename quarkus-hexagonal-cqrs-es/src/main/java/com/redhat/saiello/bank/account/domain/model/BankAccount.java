package com.redhat.saiello.bank.account.domain.model;

import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import com.redhat.saiello.bank.account.domain.model.events.AmountDeposited;
import com.redhat.saiello.bank.account.domain.model.events.AmountWithdrawn;
import com.redhat.saiello.es.AggregateRoot;
import com.redhat.saiello.es.Event;

import java.util.UUID;

import static com.redhat.saiello.es.JacksonSerDe.deserialize;

public class BankAccount extends AggregateRoot {

    public static final String TYPE = "BankAccount";

    PersonalInfo profile;
    Double balance = 0d;

    public BankAccount(String id) {
        super(id, TYPE);
    }

    public static BankAccount create(PersonalInfo profile){
        var id = UUID.randomUUID().toString();
        var aggregate = new BankAccount(id);
        aggregate.apply(new BankAccountCreated(profile));
        return aggregate;
    }


    public void withdraw(Double amount){
        if(balance - amount < 0){
            throw new RuntimeException("Insufficient balance");
        }

        apply(new AmountWithdrawn(amount));
    }

    public void deposit(Double amount){
        apply(new AmountDeposited(amount));
    }


    protected void when(Event e){
        switch (e.getType()){
            case BankAccountCreated.TYPE -> handle(deserialize(e.getPayload(), BankAccountCreated.class));
            case AmountWithdrawn.TYPE -> handle(deserialize(e.getPayload(), AmountWithdrawn.class));
            case AmountDeposited.TYPE -> handle(deserialize(e.getPayload(), AmountDeposited.class));
        }
    }

    void handle(BankAccountCreated e){
        profile = e.getPersonalInfo();
    }

    void handle(AmountWithdrawn e){
        balance = balance - e.getAmount();
    }

    void handle(AmountDeposited e){
        balance = balance + e.getAmount();
    }

}
