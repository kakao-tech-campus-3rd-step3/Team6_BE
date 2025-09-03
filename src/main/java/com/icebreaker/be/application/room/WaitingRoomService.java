package com.icebreaker.be.application.room;

import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.domain.room.*;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomService {

    private final WaitingRoomRepository waitingRoomRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final WaitingRoomIdGenerator idGenerator;

    private final WaitingRoomNotifier notifier;
    private final WaitingRoomEventPublisher eventPublisher;

    @Transactional
    public String createRoom(CreateRoomCommand cmd, Long userId) {
        String roomId = idGenerator.generate();
        waitingRoomRepository.save(
                new WaitingRoom(roomId, cmd.name(), cmd.maxParticipants()));
        joinRoom(roomId, userId);
        return roomId;
    }

    @Transactional
    public void joinRoom(String roomId, Long userId) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WAITING_ROOM_NOT_FOUND));

        userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        WaitingRoomStatus status = waitingRoom.join(userId);
        waitingRoomRepository.save(waitingRoom);

        notifier.notifyStatus(waitingRoom, status);
        if (status == WaitingRoomStatus.FULL) {
            eventPublisher.publishRoomFullEvent(waitingRoom.getRoomId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWaitingRoomFullEvent(WaitingRoomFullEvent event) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(event.roomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.WAITING_ROOM_NOT_FOUND));

        List<User> users = userRepository.findAllById(waitingRoom.getParticipants());
        Room room = new Room(waitingRoom.getName(), users.size(), new ArrayList<>());

        List<Participant> participants = users.stream()
                .map(user -> new Participant(room, user))
                .toList();

        room.joinParticipants(participants);

        roomRepository.save(room);
        waitingRoomRepository.delete(waitingRoom);
        notifier.notifyRoomStart(room);
    }
}