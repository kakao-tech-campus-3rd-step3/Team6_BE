package com.icebreaker.be.infra.persistence.redis.waitingroom;

import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.infra.persistence.redis.RedisArgs;
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
            WaitingRoom waitingRoom,
            WaitingRoomParticipant creator
    ) {
        return new CreateRoomArgs(
                waitingRoom.roomId(),
                waitingRoom.name(),
                waitingRoom.capacity(),
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
