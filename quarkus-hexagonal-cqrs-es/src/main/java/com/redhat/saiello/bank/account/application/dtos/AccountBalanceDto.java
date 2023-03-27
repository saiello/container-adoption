package com.redhat.saiello.bank.account.application.dtos;

import java.time.Instant;

public record AccountBalanceDto(Double balance, Instant updatedAt) {
}
