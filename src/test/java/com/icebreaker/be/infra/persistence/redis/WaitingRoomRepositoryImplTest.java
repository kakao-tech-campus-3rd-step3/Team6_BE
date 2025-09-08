package com.icebreaker.be.infra.persistence.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
import com.icebreaker.be.infra.persistence.redis.waitingroom.CreateRoomArgs;
import com.icebreaker.be.infra.persistence.redis.waitingroom.JoinRoomArgs;
import com.icebreaker.be.infra.persistence.redis.waitingroom.WaitingRoomRepositoryImpl;
import com.icebreaker.be.infra.persistence.redis.waitingroom.WaitingRoomStatusMapper;
import java.time.LocalDateTime;
import java.util.List;
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
    private ScriptExecutor executor;

    @Mock
    private WaitingRoomStatusMapper statusMapper;

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
        WaitingRoom waitingRoom = new WaitingRoom("test-room", "테스트 방", 3);

        when(executor.execute(any(RedisScriptEnum.class), anyList(), any(CreateRoomArgs.class),
                eq(Void.class), any(WaitingRoomStatusMapper.class)))
                .thenReturn(null);

        // when
        waitingRoomRepository.initWaitingRoom(waitingRoom, testParticipant);

        // then
        verify(executor).execute(any(RedisScriptEnum.class), anyList(), any(CreateRoomArgs.class),
                eq(Void.class), any(WaitingRoomStatusMapper.class));
    }

    @Test
    @DisplayName("대기실 참가 시 Redis 스크립트가 실행된다")
    void joinRoom_ExecutesRedisScript() {
        // given
        String roomId = "test-room";
        WaitingRoomWithParticipantIds mockResult = new WaitingRoomWithParticipantIds(null, null,
                List.of());

        when(executor.execute(any(RedisScriptEnum.class), anyList(), any(JoinRoomArgs.class),
                eq(WaitingRoomWithParticipantIds.class), any(WaitingRoomStatusMapper.class)))
                .thenReturn(mockResult);

        // when
        WaitingRoomWithParticipantIds result = waitingRoomRepository.joinWaitingRoom(roomId,
                testParticipant);

        // then
        verify(executor).execute(any(RedisScriptEnum.class), anyList(), any(JoinRoomArgs.class),
                eq(WaitingRoomWithParticipantIds.class), any(WaitingRoomStatusMapper.class));

        assertThat(result).isEqualTo(mockResult);
    }
}
