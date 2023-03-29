package com.redhat.saiello.bank.account.domain.model;

import com.redhat.saiello.bank.account.domain.model.events.AmountDeposited;
import com.redhat.saiello.bank.account.domain.model.events.AmountWithdrawn;
import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import com.redhat.saiello.common.ResultWithEvents;
import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Entity
public class BankAccount extends PanacheEntityBase {

    @Id
    String id;
    String firstName;
    String lastName;
    Double balance = 0d;


    public static ResultWithEvents<BankAccount> create(PersonalInfo profile){
        var id = UUID.randomUUID().toString();
        var aggregate = new BankAccount();
        aggregate.id = id;
        aggregate.firstName = profile.firstName();
        aggregate.lastName = profile.lastName();
        var event = new BankAccountCreated(id, new BankAccountCreated.Payload(profile));
        return new ResultWithEvents<>(aggregate, List.of(event));
    }


    public List<? extends ExportedEvent<?, ?>> withdraw(Double amount){
        if(balance - amount < 0){
            throw new RuntimeException("Insufficient balance");
        }

        balance = balance - amount;
        var event = new AmountWithdrawn(id, new AmountWithdrawn.Payload(amount));
        return List.of(event);
    }

    public List<? extends ExportedEvent<?, ?>> deposit(Double amount){
        balance = balance + amount;

        var event = new AmountDeposited(id, new AmountDeposited.Payload(amount));
        return List.of(event);
    }

    public String id() {
        return id;
    }
}
