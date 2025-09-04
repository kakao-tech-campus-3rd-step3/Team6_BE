package com.icebreaker.be.infra.persistence.redis;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.domain.room.repo.WaitingRoomRepository;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
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

    private final RedisScriptExecutor executor;
    private final ObjectMapper objectMapper;

    @Override
    public void createRoom(String roomId, String roomName, int capacity,
            WaitingRoomParticipant creator) {

        List<String> keys = List.of(participantsKey(roomId), metaKey(roomId));
        CreateRoomArgs args = CreateRoomArgs.from(roomId, roomName, capacity, creator);
        executor.execute(RedisScriptEnum.CREATE_ROOM, keys, args);
    }

    @Override
    public WaitingRoomWithParticipantIds joinRoom(String roomId,
            WaitingRoomParticipant participant) {

        List<String> keys = List.of(participantsKey(roomId), metaKey(roomId));
        JoinRoomArgs args = JoinRoomArgs.from(roomId, participant);

        String resultStr = executor.execute(RedisScriptEnum.JOIN_ROOM, keys, args);
        try {
            RedisResult<WaitingRoomResponseStatus, WaitingRoomWithParticipantIds> result = objectMapper.readValue(
                    resultStr,
                    new TypeReference<>() {
                    }
            );
            WaitingRoomResponseStatus status = result.status();
            if (!status.isSuccess()) {
                throwIfErrorResult(status);
            }
            return result.data();
        } catch (Exception e) {
            log.error("Failed to parse Redis script JSON", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String participantsKey(String roomId) {
        return PREFIX + roomId + PARTICIPANTS_SUFFIX;
    }

    private String metaKey(String roomId) {
        return PREFIX + roomId + META_SUFFIX;
    }

    private void throwIfErrorResult(WaitingRoomResponseStatus status) {
        switch (status) {
            case ROOM_NOT_FOUND -> throw new BusinessException(ErrorCode.WAITING_ROOM_NOT_FOUND);
            case ALREADY_JOINED -> throw new BusinessException(ErrorCode.ALREADY_ROOM_JOIN);
            case FULL -> throw new BusinessException(ErrorCode.WAITING_ROOM_FULL);
            case ROOM_ALREADY_EXISTS -> throw new BusinessException(ErrorCode.ROOM_ALREADY_EXISTS);
            case UNKNOWN -> throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private enum WaitingRoomResponseStatus {
        CREATED,            // 방 생성 성공
        ROOM_ALREADY_EXISTS, // 방 이미 존재
        JOINED,             // 참가 성공
        ALREADY_JOINED,     // 이미 참여한 경우
        FULL,               // 방이 꽉 찬 경우
        ROOM_NOT_FOUND,      // 방이 존재하지 않음,
        @JsonEnumDefaultValue
        UNKNOWN;

        public boolean isSuccess() {
            return this == CREATED || this == JOINED;
        }
    }

}
