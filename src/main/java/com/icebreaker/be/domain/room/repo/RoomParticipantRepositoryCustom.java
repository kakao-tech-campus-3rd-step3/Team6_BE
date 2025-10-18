package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.vo.RoomParticipantInterest;
import java.util.List;

public interface RoomParticipantRepositoryCustom {

    List<RoomParticipantInterest> findUserWithInterestsByRoomCode(String roomCode);
}
