package com.icebreaker.be.application.room.event;

import com.icebreaker.be.application.room.notifier.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.domain.room.entity.Room;
import com.icebreaker.be.domain.room.entity.RoomParticipant;
import com.icebreaker.be.domain.room.repo.RoomRepository;
import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WaitingRoomEventListener {

    private final WaitingRoomWebSocketNotifier notifier;

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @EventListener
    public void handleWaitingRoomParticipantJoinedEvent(WaitingRoomParticipantJoinedEvent event) {
        try {
            notifier.notifyParticipantJoined(event.roomId(), event.participant());
            log.info("참여자 입장 알림이 브로드캐스트되었습니다. roomId: {}",
                    event.roomId());
        } catch (Exception e) {
            log.error("참여자 입장 알림 처리 중 오류 발생. roomId: {}",
                    event.roomId(), e);
        }
    }

    @EventListener
    public void handleWaitingRoomFullEvent(WaitingRoomFullEvent event) {
        var waitingRoomWithParticipants = event.waitingRoomWithParticipantIds();
        WaitingRoom waitingRoom = waitingRoomWithParticipants.room();
        List<Long> participantIds = waitingRoomWithParticipants.participants();
        Room room = new Room(waitingRoom.roomId(), waitingRoom.name(), waitingRoom.capacity());
        List<User> user = userRepository.findAllById(participantIds);

        List<RoomParticipant> participants = user.stream()
                .map(participant -> new RoomParticipant(room, participant))
                .toList();

        room.joinParticipants(participants);
        roomRepository.save(room);

        notifier.notifyRoomStarted(waitingRoom.roomId());
    }


}
