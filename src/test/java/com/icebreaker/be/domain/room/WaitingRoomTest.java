package com.icebreaker.be.domain.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingRoomTest {

    @Test
    @DisplayName("사용자가 대기실에 참가하면 참가자 목록에 추가된다")
    void joinUserToWaitingRoom() {
        // given
        WaitingRoom waitingRoom = new WaitingRoom("room-id", "test-room", 3);
        Long userId = 1L;

        // when
        WaitingRoomStatus status = waitingRoom.join(userId);

        // then
        assertThat(status).isEqualTo(WaitingRoomStatus.WAITING);
        assertThat(waitingRoom.getParticipants()).contains(userId);
    }

    @Test
    @DisplayName("대기실이 최대 인원에 도달하면 FULL 상태를 반환한다")
    void returnFullStatusWhenReachMaxCapacity() {
        // given
        WaitingRoom waitingRoom = new WaitingRoom("room-id", "test-room", 2);
        Long firstUserId = 1L;
        Long secondUserId = 2L;

        // when
        waitingRoom.join(firstUserId);
        WaitingRoomStatus status = waitingRoom.join(secondUserId);

        // then
        assertThat(status).isEqualTo(WaitingRoomStatus.FULL);
        assertThat(waitingRoom.getParticipants()).containsExactlyInAnyOrder(firstUserId, secondUserId);
    }

    @Test
    @DisplayName("이미 최대 인원에 도달한 대기실에 사용자가 참가해도 FULL 상태를 반환한다")
    void returnFullStatusWhenAlreadyFull() {
        // given
        WaitingRoom waitingRoom = new WaitingRoom("room-id", "test-room", 2);
        Long firstUserId = 1L;
        Long secondUserId = 2L;
        Long thirdUserId = 3L;

        // when
        waitingRoom.join(firstUserId);
        waitingRoom.join(secondUserId);
        WaitingRoomStatus status = waitingRoom.join(thirdUserId);

        // then
        assertThat(status).isEqualTo(WaitingRoomStatus.FULL);
        assertThat(waitingRoom.getParticipants()).contains(thirdUserId);
    }

    @Test
    @DisplayName("대기실이 가득 찼는지 확인한다")
    void checkIfWaitingRoomIsFull() {
        // given
        WaitingRoom waitingRoom = new WaitingRoom("room-id", "test-room", 2);
        Long firstUserId = 1L;
        Long secondUserId = 2L;

        // when
        waitingRoom.join(firstUserId);
        assertThat(waitingRoom.isFull()).isFalse();

        waitingRoom.join(secondUserId);

        // then
        assertThat(waitingRoom.isFull()).isTrue();
    }

    @Test
    @DisplayName("같은 사용자가 여러 번 참가해도 중복 없이 한 번만 추가된다")
    void shouldAddUserOnlyOnce() {
        // given
        WaitingRoom waitingRoom = new WaitingRoom("room-id", "test-room", 3);
        Long userId = 1L;

        // when
        waitingRoom.join(userId);
        waitingRoom.join(userId);
        waitingRoom.join(userId);

        // then
        assertThat(waitingRoom.getParticipants()).hasSize(1);
        assertThat(waitingRoom.getParticipants()).containsExactly(userId);
    }
}
