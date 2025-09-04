package com.icebreaker.be.infra.persistence.redis;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import java.time.ZoneOffset;

public record JoinRoomArgs(
        String roomId,
        Long userId,
        String userName,
        long joinedAt
) implements RedisArgs {

    public static JoinRoomArgs from(String roomId, WaitingRoomParticipant participant) {
        return new JoinRoomArgs(
                roomId,
                participant.userId(),
                participant.userName(),
                participant.joinedAt().toEpochSecond(ZoneOffset.UTC)
        );
    }

    @Override
    public Object[] toArray() {
        return new Object[]{
                roomId,
                String.valueOf(userId),
                userName,
                String.valueOf(joinedAt)};
    }
}
