package com.icebreaker.be.infra.persistence.redis.message;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantJoinedMessage {

    private String roomId;
    private WaitingRoomWithParticipants waitingRoomWithParticipants;
}
