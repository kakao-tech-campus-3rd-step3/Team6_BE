package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomWithParticipantIds;

public interface WaitingRoomRepository {

    void initWaitingRoom(WaitingRoom waitingRoom, WaitingRoomParticipant creator);

    WaitingRoomWithParticipantIds joinWaitingRoom(String roomId,
            WaitingRoomParticipant participant);
}
