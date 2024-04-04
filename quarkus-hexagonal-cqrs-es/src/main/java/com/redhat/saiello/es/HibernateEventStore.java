package com.redhat.saiello.es;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HibernateEventStore implements EventStore{

    @Override
    public Uni<Void> saveEvents(List<Event> events) {
        return PanacheEntity.persist(events);
    }

    @Override
    public Uni<List<Event>> loadEvents(String aggregateType, String aggregateId) {
        return Event.list("aggregateType = ?1 and aggregateId = ?2", aggregateType, aggregateId);
    }

    public Uni<Optional<Object>> loadLastSnapshot(String aggregateType, String aggregateId) {
        return null; // Snapshot.list("aggregateType = ?1 and aggregateId = ?2 and version > ?3", aggregateType, aggregateId, version);
    }

    public Uni<List<Event>> loadEvents(String aggregateType, String aggregateId, long version) {
        return Event.list("aggregateType = ?1 and aggregateId = ?2 and version > ?3", aggregateType, aggregateId, version);
    }
}

/*
 *
 * e1
 * e2
 * e3
 *   snap ( v3 )
 * e4
 * e5
 *   snap ( v5 )
 * e6
 *
 *
 */