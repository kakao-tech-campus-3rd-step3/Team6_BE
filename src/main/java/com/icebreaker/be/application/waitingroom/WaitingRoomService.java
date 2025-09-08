package com.icebreaker.be.application.waitingroom;

import com.icebreaker.be.application.waitingroom.dto.CreateWaitingRoomCommand;
import com.icebreaker.be.application.waitingroom.dto.WaitingRoomId;
import com.icebreaker.be.application.waitingroom.event.WaitingRoomEventPublisher;
import com.icebreaker.be.domain.room.service.WaitingRoomIdGenerator;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoom;
import com.icebreaker.be.domain.waitingroom.WaitingRoomParticipant;
import com.icebreaker.be.domain.waitingroom.WaitingRoomRepository;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipantIds;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomService {

    private final WaitingRoomRepository waitingRoomRepository;
    private final WaitingRoomIdGenerator waitingRoomIdGenerator;
    private final WaitingRoomEventPublisher waitingRoomEventPublisher;

    private final UserRepository userRepository;

    @Transactional
    public WaitingRoomId createRoom(CreateWaitingRoomCommand cmd, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        WaitingRoomParticipant creator = new WaitingRoomParticipant(
                userId,
                user.getName(),
                LocalDateTime.now()
        );

        String roomId = waitingRoomIdGenerator.generate();
        WaitingRoom waitingRoom = new WaitingRoom(roomId, cmd.name(), cmd.capacity());

        waitingRoomRepository.initWaitingRoom(waitingRoom, creator);

        return WaitingRoomId.of(roomId);
    }

    @Transactional
    public void joinRoom(String roomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        WaitingRoomParticipant participant = new WaitingRoomParticipant(
                userId,
                user.getName(),
                LocalDateTime.now()
        );
        WaitingRoomWithParticipantIds waitingRoomWithParticipantIds = waitingRoomRepository.joinWaitingRoom(
                roomId, participant);

        waitingRoomEventPublisher.publishJoined(roomId, participant);
        if (waitingRoomWithParticipantIds.status().isFull()) {
            waitingRoomEventPublisher.publishFulled(waitingRoomWithParticipantIds);
        }
    }

}