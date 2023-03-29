package com.redhat.saiello.bank.account.application;

import com.redhat.saiello.bank.account.application.dtos.AccountBalanceDto;
import com.redhat.saiello.bank.account.projection.balance.BankAccountBalance;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class BankQueryHandler {

    public Uni<AccountBalanceDto> getAccountBalance(String accountId){
        return BankAccountBalance.<BankAccountBalance>findById(accountId)
                .onItem().ifNotNull().transform(entity -> new AccountBalanceDto(entity.getBalance(), entity.getLastUpdate()))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

}
