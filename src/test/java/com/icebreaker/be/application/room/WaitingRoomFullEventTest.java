package com.icebreaker.be.application.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingRoomFullEventTest {

    @Test
    @DisplayName("WaitingRoomFullEvent는 roomId를 포함한다")
    void eventContainsRoomId() {
        // given
        String roomId = "test-room-id";

        // when
        WaitingRoomFullEvent event = new WaitingRoomFullEvent(roomId);

        // then
        assertThat(event.roomId()).isEqualTo(roomId);
    }

    @Test
    @DisplayName("같은 roomId를 가진 두 이벤트는 동등하다")
    void eventsWithSameRoomIdAreEqual() {
        // given
        String roomId = "test-room-id";

        // when
        WaitingRoomFullEvent event1 = new WaitingRoomFullEvent(roomId);
        WaitingRoomFullEvent event2 = new WaitingRoomFullEvent(roomId);

        // then
        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }

    @Test
    @DisplayName("다른 roomId를 가진 두 이벤트는 동등하지 않다")
    void eventsWithDifferentRoomIdAreNotEqual() {
        // given
        String roomId1 = "test-room-id-1";
        String roomId2 = "test-room-id-2";

        // when
        WaitingRoomFullEvent event1 = new WaitingRoomFullEvent(roomId1);
        WaitingRoomFullEvent event2 = new WaitingRoomFullEvent(roomId2);

        // then
        assertThat(event1).isNotEqualTo(event2);
    }
}
