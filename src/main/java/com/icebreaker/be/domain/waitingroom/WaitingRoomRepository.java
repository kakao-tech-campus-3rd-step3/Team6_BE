package com.icebreaker.be.domain.waitingroom;

public interface WaitingRoomRepository {

    void initWaitingRoom(WaitingRoom waitingRoom, WaitingRoomParticipant creator);

    WaitingRoomWithParticipantIds joinWaitingRoom(String roomId,
            WaitingRoomParticipant participant);
}
