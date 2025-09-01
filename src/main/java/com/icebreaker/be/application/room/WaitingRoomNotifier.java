package com.icebreaker.be.application.room;

import com.icebreaker.be.domain.room.Room;
import com.icebreaker.be.domain.room.WaitingRoom;
import com.icebreaker.be.domain.room.WaitingRoomStatus;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingRoomNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyStatus(WaitingRoom waitingRoom, WaitingRoomStatus status) {
        messagingTemplate.convertAndSend(
                "/topic/waiting-room/" + waitingRoom.getRoomId(),
                Map.of(
                        "status", status.name().toLowerCase(),
                        "participants", waitingRoom.getParticipants()
                )
        );
    }

    public void notifyRoomStart(Room room) {
        messagingTemplate.convertAndSend(
                "/topic/room-start/" + room.getId(),
                Map.of(
                        "message", "Room started!",
                        "participants", room.getParticipants().stream()
                                .map(p -> p.getUser().getId())
                                .toList()
                )
        );
    }
}

