package com.icebreaker.be.infra.persistence.redis.message;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantJoinedMessage {

    private String roomId;
    private WaitingRoomWithParticipants waitingRoomWithParticipants;
}
