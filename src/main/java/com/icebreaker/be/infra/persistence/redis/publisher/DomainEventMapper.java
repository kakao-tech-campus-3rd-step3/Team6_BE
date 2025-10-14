package com.icebreaker.be.infra.persistence.redis.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomFullEvent;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomParticipantJoinedEvent;
import com.icebreaker.be.infra.persistence.redis.message.ParticipantJoinedMessage;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessage;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;
import com.icebreaker.be.infra.persistence.redis.message.RoomStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventMapper {

    private final ObjectMapper objectMapper;

    public PubSubMessage<?> toPubSubMessage(Object event) {
        if (event instanceof WaitingRoomParticipantJoinedEvent joinedEvent) {
            ParticipantJoinedMessage payload = new ParticipantJoinedMessage(
                    joinedEvent.roomId(),
                    joinedEvent.waitingRoomWithParticipants()
            );
            return new PubSubMessage<>(PubSubMessageType.PARTICIPANT_JOINED, payload);
        } else if (event instanceof WaitingRoomFullEvent fullEvent) {
            String roomId = fullEvent.getWaitingRoom().roomId();
            RoomStartedMessage payload = new RoomStartedMessage(roomId);
            return new PubSubMessage<>(PubSubMessageType.ROOM_STARTED, payload);
        } else {
            throw new IllegalArgumentException("unsupported event type");
        }
    }
}
