package com.redhat.saiello.es;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class EventStoreRely {

    @Inject
    @Channel("domain-event")
    Emitter<String> emitter;


    @Scheduled(delay = 5, delayUnit = TimeUnit.SECONDS, every="20s")
    @ReactiveTransactional
    public Uni<Void> publishEvents(){
        return Event.<Event>stream("publishedAt is null").onItem()
                .invoke(event -> log.info("Publishing event id={} aggregateId={} aggregateId={}", event.id, event.aggregateType, event.aggregateId))
                .invoke(event -> {
                    log.info("Sending to Kafka");
                    emitter.send(Message.of(event.payload)
                            .addMetadata(OutgoingKafkaRecordMetadata.builder()
                                .withKey(event.aggregateId)
                                .withTopic(event.aggregateType)
                                .withTimestamp(event.timestamp)
                                .withHeaders(new RecordHeaders()
                                    .add("Type", event.type.getBytes())
                                )
                                .build()));
                })
                .map(event -> event.id)
                .collect().asList().onItem()
                .call(events -> {
                    if (events.isEmpty()){
                        log.info("No events");
                        return Uni.createFrom().voidItem();
                    }
                    log.info("Updating Events");
                    return Event.update("publishedAt = ?1 where id IN(?2)", Instant.now(), events);
                }).replaceWithVoid();
    }



}
