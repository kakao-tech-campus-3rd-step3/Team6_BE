package com.icebreaker.be.infra.persistence.redis;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import java.time.ZoneOffset;

public record JoinRoomArgs(
        Long userId,
        String userName,
        long joinedAt
) implements RedisArgs {

    public static JoinRoomArgs from(WaitingRoomParticipant participant) {
        return new JoinRoomArgs(
                participant.userId(),
                participant.userName(),
                participant.joinedAt().toEpochSecond(ZoneOffset.UTC)
        );
    }

    @Override
    public Object[] toArray() {
        return new Object[]{
                String.valueOf(userId),
                userName,
                String.valueOf(joinedAt)};
    }
}
