package com.redhat.saiello.common;

import io.debezium.outbox.quarkus.ExportedEvent;

import java.util.List;

public class ResultWithEvents<T> {

    private final T result;
    private final List<? extends ExportedEvent<?,?>> events;

    public ResultWithEvents(T result, List<? extends ExportedEvent<?,?>> events) {
        this.result = result;
        this.events = events;
    }

    public T result(){
        return result;
    }

    public List<? extends ExportedEvent<?,?>> events(){
        return events;
    }
}
