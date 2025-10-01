package com.icebreaker.be.application.room.event;

import com.icebreaker.be.application.room.dto.RoomParticipantCommand;
import java.util.List;

public record RoomParticipantsEvent(
        String roomCode,
        List<RoomParticipantCommand> participants
) {

    public static RoomParticipantsEvent of(String roomCode,
            List<RoomParticipantCommand> participants) {
        return new RoomParticipantsEvent(roomCode, participants);
    }
}
