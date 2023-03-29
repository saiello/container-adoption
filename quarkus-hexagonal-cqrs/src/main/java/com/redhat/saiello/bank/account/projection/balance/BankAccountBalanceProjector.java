package com.redhat.saiello.bank.account.projection.balance;

import com.redhat.saiello.bank.account.domain.model.events.AmountDeposited;
import com.redhat.saiello.bank.account.domain.model.events.AmountWithdrawn;
import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;

import java.time.Instant;

import static com.redhat.saiello.common.JacksonSerDe.deserialize;

@Slf4j
@ApplicationScoped
public class BankAccountBalanceProjector {

    @Incoming("bank-account-event")
    @ActivateRequestContext
    public Uni<Void> onBankAccountEvent(KafkaRecord<String, String> event){

        var eventType = new String(event.getHeaders().lastHeader("eventType").value());
        var instant= event.getTimestamp();
        var aggregateId = event.getKey();
        var payload = event.getPayload()
                .replaceAll("\\\\", "") // FIXME remove all replaces dued to json encoding
                .replaceFirst("^\"", "")
                .replaceFirst("\"$", "");


        log.info("Received an event aggregateId: {}, eventType: {}, payload: '{}'", aggregateId, eventType, payload);


        return switch (eventType) {
            case BankAccountCreated.TYPE ->
                    project(aggregateId, instant, deserialize(payload, BankAccountCreated.Payload.class));
            case AmountDeposited.TYPE ->
                    project(aggregateId, instant, deserialize(payload, AmountDeposited.Payload.class));
            case AmountWithdrawn.TYPE ->
                    project(aggregateId, instant, deserialize(payload, AmountWithdrawn.Payload.class));
            default -> Uni.createFrom().voidItem();
        };
    }



    Uni<Void> project(String aggregateId, Instant occurredOn, BankAccountCreated.Payload event) {
        log.info("Project event: {}", event);
        var accountBalance = new BankAccountBalance();
        accountBalance.setAccountId(aggregateId);
        accountBalance.setBalance(0d);
        accountBalance.setLastUpdate(occurredOn);
        return accountBalance.persist().replaceWithVoid();
    }

    Uni<Void> project(String aggregateId, Instant occurredOn, AmountDeposited.Payload event) {
        log.info("Project event: {}", event);
        return BankAccountBalance.<BankAccountBalance>findById(aggregateId).onItem()
                .invoke(entity -> {
                    if(occurredOn.isAfter(entity.getLastUpdate())){
                        var balance = entity.getBalance() + event.amount();
                        entity.setLastUpdate(occurredOn);
                        entity.setBalance(balance);
                    }
                }).replaceWithVoid();
    }

    Uni<Void> project(String aggregateId, Instant occurredOn, AmountWithdrawn.Payload event) {
        log.info("Project event: {}", event);
        return BankAccountBalance.<BankAccountBalance>findById(aggregateId).onItem()
                .invoke(entity -> {
                    if(occurredOn.isAfter(entity.getLastUpdate())){
                        var balance = entity.getBalance() - event.amount();
                        entity.setLastUpdate(occurredOn);
                        entity.setBalance(balance);
                    }
                }).replaceWithVoid();
    }


}
