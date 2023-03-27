package com.redhat.saiello.bank.account.domain.model.commands;

public record DepositCommand(String accountId, Double amount) {
    
}
