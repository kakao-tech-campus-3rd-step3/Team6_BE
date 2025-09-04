package com.icebreaker.be.application.room.notifier;

import com.icebreaker.be.domain.room.vo.WaitingRoom;
import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingRoomWebSocketNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyRoomCreated(WaitingRoom waitingRoom) {
        messagingTemplate.convertAndSend(
                "/topic/waiting-room/" + waitingRoom.roomId(),
                Map.of(
                        "type", "ROOM_CREATED",
                        "roomId", waitingRoom.roomId(),
                        "name", waitingRoom.name(),
                        "capacity", waitingRoom.capacity()
                )
        );
    }

    public void notifyParticipantJoined(String roomId,
            WaitingRoomParticipant newParticipant) {
        messagingTemplate.convertAndSend(
                "/topic/waiting-room/" + roomId,
                Map.of(
                        "type", "PARTICIPANT_JOINED",
                        "newParticipant", Map.of(
                                "userId", newParticipant.userId(),
                                "userName", newParticipant.userName(),
                                "joinedAt", newParticipant.joinedAt().toString()
                        )
                )
        );
    }

    public void notifyRoomStarted(String roomId) {
        messagingTemplate.convertAndSend(
                "/topic/waiting-room/" + roomId,
                Map.of(
                        "type", "ROOM_STARTED",
                        "roomId", roomId
                )
        );
    }
}
