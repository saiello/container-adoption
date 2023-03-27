package com.redhat.saiello.bank.account.domain.model.commands;

public record WithdrawCommand(String accountId, Double amount) {
}
