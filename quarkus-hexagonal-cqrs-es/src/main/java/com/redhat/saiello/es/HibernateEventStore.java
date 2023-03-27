package com.redhat.saiello.es;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

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
}
