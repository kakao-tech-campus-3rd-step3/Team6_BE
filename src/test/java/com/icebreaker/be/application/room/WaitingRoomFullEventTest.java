package com.icebreaker.be.application.room;

import static org.assertj.core.api.Assertions.assertThat;

import com.icebreaker.be.application.waitingroom.event.WaitingRoomFullEvent;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomStatus;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WaitingRoomFullEventTest {

    @Test
    @DisplayName("WaitingRoomFullEvent는 WaitingRoomWithParticipantIds를 포함한다")
    void eventContainsWaitingRoomWithParticipantIds() {
        // given
        WaitingRoom room = new WaitingRoom("test-room-id", "테스트 방", 3);
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                room,
                List.of(1L, 2L, 3L)
        );

        // when
        WaitingRoomFullEvent event = new WaitingRoomFullEvent(waitingRoomWithParticipantIds);

        // then
        assertThat(event.waitingRoomWithParticipantIds()).isEqualTo(waitingRoomWithParticipantIds);
        assertThat(event.waitingRoomWithParticipantIds().room().roomId()).isEqualTo("test-room-id");
        assertThat(event.waitingRoomWithParticipantIds().status()).isEqualTo(
                WaitingRoomStatus.FULL);
    }

    @Test
    @DisplayName("같은 WaitingRoomWithParticipantIds를 가진 두 이벤트는 동등하다")
    void eventsWithSameDataAreEqual() {
        // given
        WaitingRoom room = new WaitingRoom("test-room-id", "테스트 방", 3);
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                room,
                List.of(1L, 2L, 3L)
        );

        // when
        WaitingRoomFullEvent event1 = new WaitingRoomFullEvent(waitingRoomWithParticipantIds);
        WaitingRoomFullEvent event2 = new WaitingRoomFullEvent(waitingRoomWithParticipantIds);

        // then
        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    @DisplayName("다른 데이터를 가진 두 이벤트는 동등하지 않다")
    void eventsWithDifferentDataAreNotEqual() {
        // given
        WaitingRoom room1 = new WaitingRoom("test-room-id-1", "테스트 방1", 3);
        WaitingRoom room2 = new WaitingRoom("test-room-id-2", "테스트 방2", 3);

        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds1 = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                room1,
                List.of(1L, 2L, 3L)
        );

        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds2 = new WaitingRoomWithParticipantIds(
                WaitingRoomStatus.FULL,
                room2,
                List.of(1L, 2L, 3L)
        );

        // when
        WaitingRoomFullEvent event1 = new WaitingRoomFullEvent(waitingRoomWithParticipantIds1);
        WaitingRoomFullEvent event2 = new WaitingRoomFullEvent(waitingRoomWithParticipantIds2);

        // then
        assertThat(event1).isNotEqualTo(event2);
    }
}
