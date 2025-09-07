package com.icebreaker.be.application.room.notifier;

import com.icebreaker.be.domain.room.vo.WaitingRoomParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingRoomWebSocketNotifier {

    private static final String WAITING_ROOM_TOPIC_PREFIX = "/topic/waiting-room/";

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyParticipantJoined(String roomId, WaitingRoomParticipant newParticipant) {
        WaitingRoomMessage message = new ParticipantJoinedPayload(newParticipant);
        sendToWaitingRoom(roomId, message);
    }

    public void notifyRoomStarted(String roomId) {
        WaitingRoomMessage message = new RoomStartedPayload(roomId);
        sendToWaitingRoom(roomId, message);
    }

    private void sendToWaitingRoom(String roomId, WaitingRoomMessage message) {
        String waitingRoomTopic = getWaitingRoomTopic(roomId);
        messagingTemplate.convertAndSend(waitingRoomTopic, message);
    }

    private String getWaitingRoomTopic(String roomId) {
        return WAITING_ROOM_TOPIC_PREFIX + roomId;
    }


}
