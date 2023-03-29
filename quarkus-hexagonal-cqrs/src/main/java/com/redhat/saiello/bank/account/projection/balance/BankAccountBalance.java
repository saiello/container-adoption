package com.redhat.saiello.bank.account.projection.balance;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
public class BankAccountBalance extends PanacheEntityBase {

    @Id
    String accountId;
    Double balance;
    Instant lastUpdate;

}
