package com.icebreaker.be.infra.persistence.redis;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import java.time.ZoneOffset;

public record CreateRoomArgs(
        String roomId,
        String roomName,
        int capacity,
        Long creatorUserId,
        String creatorUserName,
        long joinedAt
) implements RedisArgs {

    public static CreateRoomArgs from(
            String roomId,
            String roomName,
            int capacity,
            WaitingRoomParticipant creator
    ) {
        return new CreateRoomArgs(
                roomId,
                roomName,
                capacity,
                creator.userId(),
                creator.userName(),
                creator.joinedAt().toEpochSecond(ZoneOffset.UTC)
        );
    }

    @Override
    public Object[] toArray() {
        return new Object[]{
                roomId,
                roomName,
                String.valueOf(capacity),
                String.valueOf(creatorUserId),
                creatorUserName,
                String.valueOf(joinedAt)
        };
    }
}
