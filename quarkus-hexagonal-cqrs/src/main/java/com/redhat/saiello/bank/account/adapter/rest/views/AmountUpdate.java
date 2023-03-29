package com.redhat.saiello.bank.account.adapter.rest.views;

import javax.validation.constraints.NotNull;

public record AmountUpdate(@NotNull Double amount) {}
