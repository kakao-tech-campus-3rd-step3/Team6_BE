package com.icebreaker.be.domain.publisher;

public interface EventPublisher {

    void publish(DomainEvent event);
}
