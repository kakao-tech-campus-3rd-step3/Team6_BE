package com.icebreaker.be.infra.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public abstract class EventPublisherSupport {

    private final ApplicationEventPublisher applicationEventPublisher;

    protected void publishEvent(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
