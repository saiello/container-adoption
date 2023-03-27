package com.redhat.saiello.es;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.time.Instant;

@Data
@Entity
public class Event extends PanacheEntity {

    String aggregateType;
    String aggregateId;

    String type;
    String payload;

    long version;
    Instant timestamp;

    Instant publishedAt;
}
