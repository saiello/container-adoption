package com.redhat.saiello.es;

public interface EventPayload<T> {

    String type();

    void apply(T aggregate);
}
