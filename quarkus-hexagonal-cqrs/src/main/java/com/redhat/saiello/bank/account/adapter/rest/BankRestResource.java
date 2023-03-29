package com.redhat.saiello.bank.account.adapter.rest;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.saiello.bank.account.adapter.rest.views.AmountUpdate;
import com.redhat.saiello.bank.account.adapter.rest.views.CreateBankAccount;
import com.redhat.saiello.bank.account.application.BankCommandHandler;

import com.redhat.saiello.bank.account.application.BankQueryHandler;
import com.redhat.saiello.bank.account.application.dtos.AccountBalanceDto;
import com.redhat.saiello.bank.account.domain.model.commands.CreateAccountCommand;
import com.redhat.saiello.bank.account.domain.model.commands.DepositCommand;
import com.redhat.saiello.bank.account.domain.model.commands.WithdrawCommand;
import io.smallrye.mutiny.Uni;

@Path(value = "/api/v1/bank")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BankRestResource {

    @Inject
    BankCommandHandler commandHandler;

    @Inject
    BankQueryHandler queryHandler;

    @POST
    @Path("/account")
    public Uni<Response> create(CreateBankAccount bankAccount){
        return commandHandler.create(new CreateAccountCommand(bankAccount.firstName(), bankAccount.lastName()))
            .onItem().transform(id -> Response.ok(id).build());
    }

    @GET
    @Path("/account/{accountId}")
    public Uni<Response> balance(@PathParam("accountId") String accountId){
        return queryHandler.getAccountBalance(accountId)
                .onItem().transform(balance -> Response.ok(balance).build())
                .onFailure(NotFoundException.class).recoverWithItem(Response.status(Response.Status.NOT_FOUND).build())
                .onFailure().recoverWithItem(Response.serverError().build());
    }


    @POST
    @Path("/account/{accountId}/deposit")
    public Uni<Response> deposit(@PathParam("accountId") String accountId, AmountUpdate amountUpdate){
        return commandHandler.deposit(new DepositCommand(accountId, amountUpdate.amount()))
            .onItem().transform(v -> Response.ok().build());
    }

    @POST
    @Path("/account/{accountId}/withdraw")
    public Uni<Response> withdraw(@PathParam("accountId") String accountId, @Valid AmountUpdate amountUpdate){
        return commandHandler.withdraw(new WithdrawCommand(accountId, amountUpdate.amount()))
            .onItem().transform(v -> Response.ok().build());
    }

}
