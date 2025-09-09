package com.icebreaker.be.application.waitingroom.notifier;

import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants.Participant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingRoomWebSocketNotifier {

    private static final String WAITING_ROOM_TOPIC_PREFIX = "/topic/waiting-room/";

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyParticipantJoined(String roomId, List<Participant> participants) {
        WaitingRoomMessage message = new ParticipantJoinedPayload(participants);
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
