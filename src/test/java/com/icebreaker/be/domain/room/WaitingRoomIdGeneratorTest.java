package com.icebreaker.be.domain.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WaitingRoomIdGeneratorTest {

    @Test
    @DisplayName("WaitingRoomIdGenerator는 유효한 ID를 생성한다")
    void generateValidId() {
        // given
        WaitingRoomIdGenerator generator = new TestWaitingRoomIdGenerator();

        // when
        String roomId = generator.generate();

        // then
        assertThat(roomId).isNotNull();
        assertThat(roomId).isNotEmpty();
    }

    @Test
    @DisplayName("여러 번 호출해도 ID가 생성된다")
    void generateMultipleIds() {
        // given
        WaitingRoomIdGenerator generator = new TestWaitingRoomIdGenerator();

        // when
        String roomId1 = generator.generate();
        String roomId2 = generator.generate();
        String roomId3 = generator.generate();

        // then
        assertThat(roomId1).isNotNull();
        assertThat(roomId2).isNotNull();
        assertThat(roomId3).isNotNull();
    }

    /**
     * WaitingRoomIdGenerator의 테스트용 구현체
     */
    static class TestWaitingRoomIdGenerator implements WaitingRoomIdGenerator {
        @Override
        public String generate() {
            return "test-room-id";
        }
    }
}
