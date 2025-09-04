package com.icebreaker.be.domain.room.repo;

import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import com.icebreaker.be.domain.room.vo.WaitingRoomStatus;
import java.util.Optional;

public interface WaitingRoomRepository {

    void save(WaitingRoom room);

    Optional<WaitingRoom> findById(String roomId);

    void delete(WaitingRoom room);

    // 참가자 join - WaitingRoom을 반환하여 상태를 확인할 수 있도록 함
    WaitingRoom join(String roomId, WaitingRoomParticipant participant);
}
