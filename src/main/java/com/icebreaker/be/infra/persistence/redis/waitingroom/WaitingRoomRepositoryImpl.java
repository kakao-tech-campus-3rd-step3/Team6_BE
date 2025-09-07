package com.icebreaker.be.infra.persistence.redis.waitingroom;

import com.icebreaker.be.domain.room.repo.WaitingRoomRepository;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;
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

    @Override
    public void initWaitingRoom(WaitingRoom waitingRoom, WaitingRoomParticipant creator) {
        List<String> keys = List.of(
                participantsKey(waitingRoom.roomId()),
                metaKey(waitingRoom.roomId())
        );

        CreateRoomArgs args = CreateRoomArgs.from(waitingRoom, creator);

        executor.execute(
                RedisScriptEnum.CREATE_ROOM,
                keys,
                args,
                Void.class,
                statusMapper
        );
    }

    /**
     *
     * @param roomId      - WaitingRoom ID
     * @param participant - 참가자 정보
     * @return
     */
    @Override
    public WaitingRoomWithParticipantIds joinWaitingRoom(String roomId,
            WaitingRoomParticipant participant) {
        List<String> keys = List.of(participantsKey(roomId), metaKey(roomId));
        JoinRoomArgs args = JoinRoomArgs.from(participant);

        return executor.execute(
                RedisScriptEnum.JOIN_ROOM,
                keys,
                args,
                WaitingRoomWithParticipantIds.class,
                statusMapper
        );
    }

    private String participantsKey(String roomId) {
        return PREFIX + roomId + PARTICIPANTS_SUFFIX;
    }

    private String metaKey(String roomId) {
        return PREFIX + roomId + META_SUFFIX;
    }
}
