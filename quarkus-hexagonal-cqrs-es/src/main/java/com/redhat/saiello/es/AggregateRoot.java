package com.redhat.saiello.es;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.redhat.saiello.es.JacksonSerDe.serialize;

public abstract class AggregateRoot {

    private final String id;
    private final String type;
    private AtomicLong version = new AtomicLong(0);
    private List<Event> changes = new ArrayList<>();

    public AggregateRoot(String id, String type) {
        this.id = id;
        this.type = type;
    }


    public Long version(){ return version.get(); }
    public List<Event> changes(){
        return changes;
    }
    public String id(){
        return id;
    }

    protected void apply(EventPayload payload) {
        var domainEvent = new Event();
        domainEvent.setAggregateId(id());
        domainEvent.setAggregateType(type);
        domainEvent.setPayload(serialize(payload));
        domainEvent.setType(payload.type());
        domainEvent.setTimestamp(Instant.now());
        domainEvent.setVersion(this.version.incrementAndGet());
        apply(domainEvent);
    }

    protected void apply(Event event) {
        // add to changes
        changes.add(event);

        // mutate
        when(event);
    }

    protected abstract void when(Event e);

    public void load(List<Event> events){
        long version = 0;
        for(Event e : events){
            when(e);
            version = e.getVersion();
        }
        this.version.set(version);
    }
}
