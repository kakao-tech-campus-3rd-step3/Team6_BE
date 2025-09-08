package com.icebreaker.be.application.room;


import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.application.room.event.RoomEventPublisher;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final RoomEventPublisher publisher;

    @Transactional
    public Room create(WaitingRoom waitingRoom, List<Long> participantIds) {
        Room room = new Room(waitingRoom.roomId(), waitingRoom.name(), waitingRoom.capacity());
        List<User> users = userRepository.findAllById(participantIds);
        room.joinUsers(users);
        Room savedRoom = roomRepository.save(room);

        publisher.publishStageChanged(room.getCode(), Stage.STAGE_1);

        return savedRoom;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void changeStage(String roomCode, ChangeRoomStageCommand command) {
        if (!roomRepository.existsByCode(roomCode)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }

        publisher.publishStageChanged(roomCode, command.getStageEnum());
    }
}
