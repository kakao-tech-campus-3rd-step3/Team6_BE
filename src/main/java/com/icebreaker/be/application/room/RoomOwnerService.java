package com.icebreaker.be.application.room;

import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.repo.RoomOwnerRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomOwnerService {

    private final RoomOwnerRepository roomOwnerRepository;

    public void create(Room room) {
        roomOwnerRepository.save(room.getCode(), room.getHostId());
    }

    public void validateRoomOwner(String roomCode, Long userId) {
        Long ownerId = roomOwnerRepository.findOwnerByRoomCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_OWNER_NOT_FOUND));

        if (!ownerId.equals(userId)) {
            throw new BusinessException(ErrorCode.ROOM_OWNER_MISMATCH);
        }
    }
}
