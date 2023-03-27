package com.redhat.saiello.bank.account.domain.model;

import com.redhat.saiello.es.EventStore;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BankAccountRepository {

    @Inject
    EventStore eventStore;

    public Uni<BankAccount> findById(String accountId){
        return eventStore.loadEvents(BankAccount.class.getSimpleName(), accountId)
                .onItem().transform(events -> {
                    var aggregate = new BankAccount(accountId);
                    aggregate.load(events);
                    return aggregate;
                });

    }


    public Uni<Void> save(BankAccount account) {
        return eventStore.saveEvents(account.changes());
    }
}
