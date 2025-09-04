package com.icebreaker.be.infra.persistence.redis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaitingRoomRepositoryImplTest {

    @Mock
    private RedisScriptExecutor executor;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WaitingRoomRepositoryImpl waitingRoomRepository;

    private WaitingRoomParticipant testParticipant;
    private WaitingRoom testRoom;

    @BeforeEach
    void setUp() {
        testParticipant = new WaitingRoomParticipant(1L, "테스트 유저", LocalDateTime.now());
        testRoom = new WaitingRoom("test-room", "테스트 방", 3);
    }

    @Test
    @DisplayName("대기실 생성 시 Redis 스크립트가 실행된다")
    void createRoom_ExecutesRedisScript() {
        // given
        String roomId = "test-room";
        String roomName = "테스트 방";
        int capacity = 3;

        when(executor.execute(any(RedisScriptEnum.class), anyList(), any(CreateRoomArgs.class)))
                .thenReturn(""); // 빈 문자열 반환

        // when
        waitingRoomRepository.createRoom(roomId, roomName, capacity, testParticipant);

        // then
        verify(executor).execute(any(RedisScriptEnum.class), anyList(), any(CreateRoomArgs.class));
    }

    @Test
    @DisplayName("대기실 참가 시 Redis 스크립트가 실행된다")
    void joinRoom_ExecutesRedisScript() {
        // given
        String roomId = "test-room";

        when(executor.execute(any(RedisScriptEnum.class), anyList(), any(JoinRoomArgs.class)))
                .thenReturn("");
        // when & then
        Assertions.assertThatThrownBy(() -> waitingRoomRepository.joinRoom(roomId, testParticipant))
                .isInstanceOf(Exception.class);

        verify(executor).execute(any(RedisScriptEnum.class), anyList(), any(JoinRoomArgs.class));
    }
}
