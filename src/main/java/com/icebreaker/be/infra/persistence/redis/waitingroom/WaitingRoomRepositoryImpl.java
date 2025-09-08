package com.icebreaker.be.infra.persistence.redis.waitingroom;

import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;
import com.icebreaker.be.domain.waitingroom.WaitingRoomRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
import com.icebreaker.be.infra.persistence.redis.RedisArgs;
import com.icebreaker.be.infra.persistence.redis.RedisScriptEnum;
import com.icebreaker.be.infra.persistence.redis.ScriptExecutor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomRepositoryImpl implements WaitingRoomRepository {

    private static final String PREFIX = "waitingRoom:";
    private static final String PARTICIPANTS_SUFFIX = ":participants";
    private static final String META_SUFFIX = ":meta";

    private final ScriptExecutor executor;
    private final WaitingRoomStatusMapper statusMapper;

    /**
     * Redis에 WaitingRoom과 방장 정보를 초기화합니다. Lua Script CREATE_ROOM을 실행하며 반환값은 없습니다.
     *
     * @param waitingRoom 생성할 WaitingRoom 객체
     * @param creator     방장 참가자 정보
     */
    @Override
    public void initWaitingRoom(WaitingRoom waitingRoom, WaitingRoomParticipant creator) {
        List<String> keys = List.of(
                getParticipantsKey(waitingRoom.roomId()),
                getMetaKey(waitingRoom.roomId())
        );

        CreateRoomArgs args = CreateRoomArgs.from(waitingRoom, creator);

        log.debug("Initializing waiting room: {}", waitingRoom.roomId());

        executeScript(
                RedisScriptEnum.CREATE_ROOM,
                keys,
                args,
                Void.class
        );
    }

    /**
     * 참가자가 특정 WaitingRoom에 입장합니다. Lua Script JOIN_ROOM을 실행하고, 최신 참여자 정보를 반환합니다.
     *
     * @param roomId      입장할 WaitingRoom ID
     * @param participant 참가자 정보
     * @return WaitingRoomWithParticipantIds 최신 참가자 ID 목록 포함
     */
    @Override
    public WaitingRoomWithParticipantIds joinWaitingRoom(String roomId,
            WaitingRoomParticipant participant) {
        List<String> keys = List.of(getParticipantsKey(roomId), getMetaKey(roomId));
        JoinRoomArgs args = JoinRoomArgs.from(participant);

        log.debug("Participant {} joining room {}", participant.userId(), roomId);

        return executeScript(
                RedisScriptEnum.JOIN_ROOM,
                keys,
                args,
                WaitingRoomWithParticipantIds.class
        );
    }

    private <T> T executeScript(RedisScriptEnum script, List<String> keys, RedisArgs args,
            Class<T> clazz) {
        return executor.execute(script, keys, args, clazz, statusMapper);
    }

    private String getParticipantsKey(String roomId) {
        return PREFIX + roomId + PARTICIPANTS_SUFFIX;
    }

    private String getMetaKey(String roomId) {
        return PREFIX + roomId + META_SUFFIX;
    }
}
