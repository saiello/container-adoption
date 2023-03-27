package com.redhat.saiello.bank.account.application;

import com.redhat.saiello.bank.account.domain.model.BankAccountRepository;
import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import com.redhat.saiello.bank.account.domain.model.BankAccount;
import com.redhat.saiello.bank.account.domain.model.commands.CreateAccountCommand;
import com.redhat.saiello.bank.account.domain.model.commands.DepositCommand;
import com.redhat.saiello.bank.account.domain.model.commands.WithdrawCommand;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Slf4j
@ApplicationScoped
@ReactiveTransactional
public class BankCommandHandler {

    @Inject
    BankAccountRepository repository;
    
    public Uni<String> create(CreateAccountCommand command){
        log.info("Handle command: {}", command);
        var profile = new PersonalInfo(command.firstName(), command.lastName());
        var account = BankAccount.create(profile);
        return repository.save(account).onItem()
                .transform(v -> account.id());
    }


    public Uni<Void> deposit(DepositCommand command){
        log.info("Handle command: {}", command);
        return repository.findById(command.accountId())
                .onItem()
                .invoke(account -> account.deposit(command.amount()))
                .chain(account -> repository.save(account))
                .replaceWithVoid();
    }


    public Uni<Void> withdraw(WithdrawCommand command){
        log.info("Handle command: {}", command);
        return repository.findById(command.accountId())
                .onItem()
                .invoke(account -> account.withdraw(command.amount()))
                .chain(account -> repository.save(account))
                .replaceWithVoid();
    }

}


