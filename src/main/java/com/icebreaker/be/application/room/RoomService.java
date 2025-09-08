package com.icebreaker.be.application.room;


import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.application.room.event.RoomEventPublisher;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.RoomStage;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomStageRepository roomStageRepository;

    private final RoomEventPublisher publisher;

    @Transactional
    public Room create(WaitingRoom waitingRoom, List<Long> participantIds) {
        Room room = new Room(waitingRoom.roomId(), waitingRoom.name(), waitingRoom.capacity());
        List<User> users = userRepository.findAllById(participantIds);
        room.joinUsers(users);
        Room savedRoom = roomRepository.save(room);

        RoomStage roomStage = RoomStage.init(room.getCode());
        roomStageRepository.save(roomStage);

        publisher.publishStageChanged(room.getCode(), roomStage.currentStage());

        return savedRoom;
    }

    public void changeStage(String roomCode, ChangeRoomStageCommand command) {
        Stage newStage = command.getStageEnum();
        RoomStage roomStage = RoomStage.of(roomCode, newStage);
        roomStageRepository.save(roomStage);

        publisher.publishStageChanged(roomCode, newStage);
    }
}
