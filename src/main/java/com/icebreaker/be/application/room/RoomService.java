package com.icebreaker.be.application.room;


import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.RoomParticipant;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;


    @Transactional
    public void create(WaitingRoom waitingRoom, List<Long> participantIds) {
        Room room = new Room(waitingRoom.roomId(), waitingRoom.name(), waitingRoom.capacity());
        List<User> user = userRepository.findAllById(participantIds);

        List<RoomParticipant> participants = user.stream()
                .map(participant -> new RoomParticipant(room, participant))
                .toList();

        room.joinParticipants(participants);
        roomRepository.save(room);
    }
}
