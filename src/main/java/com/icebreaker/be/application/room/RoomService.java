package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.application.room.dto.RoomResponse;
import com.icebreaker.be.application.room.mapper.RoomMapper;
import com.icebreaker.be.domain.room.Room;
import com.icebreaker.be.domain.room.RoomRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        return roomRepository
                .findById(id)
                .map(RoomMapper::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(RoomMapper::toResponse)
                .toList();
    }

    @Transactional
    public Long createRoom(CreateRoomCommand createRoomCommand) {
        return roomRepository.save(createRoomCommand.toEntity()).getId();
    }

    @Transactional
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        roomRepository.delete(room);
    }
}
