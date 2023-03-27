package com.redhat.saiello.es;

import io.smallrye.mutiny.Uni;

import java.util.List;

public interface EventStore {


    Uni<Void> saveEvents(List<Event> events);

    Uni<List<Event>> loadEvents(String aggregateType, String aggregateId);
    
}
