package com.icebreaker.be.application.room;


import com.icebreaker.be.application.jwt.JwtService;
import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.application.room.dto.RoomTicket;
import com.icebreaker.be.application.room.dto.RoomTicketJwtClaims;
import com.icebreaker.be.application.room.event.RoomStageEventPublisher;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.room.vo.RoomParticipantRole;
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
    private final JwtService jwtService;
    private final RoomStageEventPublisher publisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Room createRoom(WaitingRoom waitingRoom, List<Long> participantIds) {
        List<User> users = userRepository.findAllById(participantIds);
        Room room = Room.from(waitingRoom);
        room.joinUsers(users);
        Room savedRoom = roomRepository.save(room);

        publisher.publishStageChanged(room.getCode(), Stage.STAGE_1);

        return savedRoom;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void changeRoomStage(String roomCode, ChangeRoomStageCommand command) {
        if (!roomRepository.existsByCode(roomCode)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }

        publisher.publishStageChanged(roomCode, command.getStageEnum());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public RoomTicket issueRoomTicket(String roomCode, Long userId) {
        RoomParticipantRole role = getUserRole(roomCode, userId);
        RoomTicketJwtClaims claims = new RoomTicketJwtClaims(roomCode, role);
        String token = jwtService.generateTokenWithClaims(String.valueOf(userId), claims);

        return new RoomTicket(userId, role, token);
    }

    private RoomParticipantRole getUserRole(String roomCode, Long userId) {
        return roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND))
                .getUserRole(userId);
    }
}
