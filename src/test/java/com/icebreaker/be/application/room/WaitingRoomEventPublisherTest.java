package com.icebreaker.be.application.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WaitingRoomEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private WaitingRoomEventPublisher waitingRoomEventPublisher;

    @Test
    @DisplayName("WaitingRoomFullEvent를 발행한다")
    void publishRoomFullEvent() {
        // given
        String roomId = "test-room-id";

        // when
        waitingRoomEventPublisher.publishRoomFullEvent(roomId);

        // then
        verify(applicationEventPublisher).publishEvent(new WaitingRoomFullEvent(roomId));
    }
}
