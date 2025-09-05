package com.icebreaker.be.application.room;

import static org.mockito.Mockito.verify;

import com.icebreaker.be.application.room.event.WaitingRoomEventPublisher;
import com.icebreaker.be.application.room.event.WaitingRoomFullEvent;
import com.icebreaker.be.application.room.event.WaitingRoomParticipantJoinedEvent;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomStatus;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class WaitingRoomEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private WaitingRoomEventPublisher waitingRoomEventPublisher;

    @Test
    @DisplayName("WaitingRoomFullEvent를 발행한다")
    void publishFullEvent() {
        // given
        WaitingRoom room = new WaitingRoom("test-room-id", "테스트 방", 3);
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                room,
                List.of(1L, 2L, 3L)
        );

        // when
        waitingRoomEventPublisher.publishFulled(waitingRoomWithParticipantIds);

        // then
        verify(applicationEventPublisher).publishEvent(
                new WaitingRoomFullEvent(waitingRoomWithParticipantIds));
    }

    @Test
    @DisplayName("WaitingRoomParticipantJoinedEvent를 발행한다")
    void publishJoinedEvent() {
        // given
        String roomId = "test-room-id";
        WaitingRoomParticipant participant = new WaitingRoomParticipant(1L, "테스트 유저",
                LocalDateTime.now());

        // when
        waitingRoomEventPublisher.publishJoined(roomId, participant);

        // then
        verify(applicationEventPublisher).publishEvent(
                new WaitingRoomParticipantJoinedEvent(roomId, participant));
    }
}
