package com.icebreaker.be.application.waitingroom.messaging;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;

public interface WaitingRoomNotifier {

    void notifyParticipantJoined(
            String roomId,
            WaitingRoomWithParticipants waitingRoomWithParticipants
    );

    void notifyRoomStarted(String roomId);
}
