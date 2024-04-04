package com.redhat.saiello.es;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.time.Instant;

@Data
@Entity


/*
 *
 * UNIQUE (aggregateType, aggregateId, version)
 *
 * * POST /bank/<ID>/deposit  amount=45
 *  thread-1   Load ( e1, e2, e3 )  version=3
 *
 * * POST /bank/<ID>/deposit  amount=67
 *  thread-2   Load ( e1, e2, e3 )  version=3
 *
 *  thread-1   Save ( Deposit(aggregateType, aggregateIt, v4 {... 45 ...})
 *
 *  thread-2   Save ( Deposit(aggregateType, aggregateIt, v4 {... 67 ...})
 *  thread-2   Commit
 *
 *  thread-1   Commit - FAIL
 */
public class Event extends PanacheEntity {

    String aggregateType;
    String aggregateId;

    String type;
    String payload;

    long version;
    Instant timestamp;

    Instant publishedAt;
}
