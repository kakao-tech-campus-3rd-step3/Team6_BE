package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;

public interface WaitingRoomRepository {

    void createRoom(String roomId, String roomName, int capacity,
            WaitingRoomParticipant creator);

    WaitingRoomWithParticipantIds joinRoom(String roomId, WaitingRoomParticipant participant);
}
