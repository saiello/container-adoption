package com.redhat.saiello.bank.account.application;

import com.redhat.saiello.bank.account.domain.model.BankAccount;
import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import com.redhat.saiello.bank.account.domain.model.commands.CreateAccountCommand;
import com.redhat.saiello.bank.account.domain.model.commands.DepositCommand;
import com.redhat.saiello.bank.account.domain.model.commands.WithdrawCommand;
import io.debezium.outbox.reactive.quarkus.internal.DebeziumOutboxHandler;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
@ReactiveTransactional
public class BankCommandHandler {

    @Inject
    DebeziumOutboxHandler handler;


    public Uni<String> create(CreateAccountCommand command){
        log.info("Handle command: {}", command);
        var profile = new PersonalInfo(command.firstName(), command.lastName());

        var resultWithEvents = BankAccount.create(profile);
        var account = resultWithEvents.result();
        var events = resultWithEvents.events();

        return account.persist()
                .chain(() -> Multi.createFrom()
                        .iterable(events).onItem()
                        .call(event -> handler.persistToOutbox(event))
                        .collect()
                        .asList())
                .onItem().transform(v -> account.id());
    }


    public Uni<Void> deposit(DepositCommand command){
        log.info("Handle command: {}", command);
        return BankAccount.<BankAccount>findById(command.accountId())
                .onItem().call(account -> {
                    var events = account.deposit(command.amount());
                    return Multi.createFrom()
                            .iterable(events).onItem()
                            .call(event -> handler.persistToOutbox(event))
                            .collect()
                            .asList();
                }).replaceWithVoid();
    }


    public Uni<Void> withdraw(WithdrawCommand command){
        log.info("Handle command: {}", command);
        return BankAccount.<BankAccount>findById(command.accountId())
                .onItem().call(account -> {
                    var events = account.withdraw(command.amount());
                    return Multi.createFrom()
                            .iterable(events).onItem()
                            .call(event -> handler.persistToOutbox(event))
                            .collect()
                            .asList();
                }).replaceWithVoid();
    }

}


