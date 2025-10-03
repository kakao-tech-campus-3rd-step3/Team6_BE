package com.icebreaker.be.application.room.messaging;

import com.icebreaker.be.application.room.dto.RoomParticipantCommand;
import java.util.List;

public interface RoomNotifier {

    void notifyRoomParticipants(String roomCode, List<RoomParticipantCommand> participants);
}
