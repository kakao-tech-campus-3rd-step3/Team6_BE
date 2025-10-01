package com.icebreaker.be.application.room;


import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.application.room.dto.RoomParticipantCommand;
import com.icebreaker.be.application.room.event.RoomStageEventPublisher;
import com.icebreaker.be.application.room.messaging.RoomNotifier;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final RoomStageEventPublisher publisher;
    private final RoomNotifier roomNotifier;
    private final RoomOwnerService roomOwnerService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Room createRoom(WaitingRoom waitingRoom, List<Long> participantIds) {
        List<User> users = loadParticipants(participantIds);

        Room room = Room.from(waitingRoom);
        room.joinUsers(users);

        Room savedRoom = roomRepository.save(room);
        roomOwnerService.create(room);

        publisher.publishStageInitialized(room.getCode());
        return savedRoom;
    }

    @Transactional(readOnly = true)
    public void changeRoomStage(String roomCode, Long userId, ChangeRoomStageCommand command) {
        validateRoomExists(roomCode);

        roomOwnerService.validateRoomOwner(roomCode, userId);

        publisher.publishStageChanged(
                roomCode,
                command.getEventTypeEnum(),
                command.getStageEnum()
        );
    }

    @Transactional(readOnly = true)
    public void sendRoomParticipants(String roomCode, Long userId) {
        validateRoomExists(roomCode);

        roomOwnerService.validateRoomOwner(roomCode, userId);

        List<RoomParticipantCommand> participants = roomRepository.findUsersByRoomCode(roomCode)
                .stream()
                .map(RoomParticipantCommand::fromEntity)
                .toList();

        roomNotifier.notifyRoomParticipants(roomCode, participants);
    }

    private List<User> loadParticipants(List<Long> participantIds) {
        return userRepository.findAllById(participantIds);
    }


    private void validateRoomExists(String roomCode) {
        if (!roomRepository.existsByCode(roomCode)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
    }
}

