package com.icebreaker.be.application.room.notify;

import com.icebreaker.be.domain.room.entity.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomStageWebSocketNotifier {

    private static final String ROOM_STAGE_TOPIC_PREFIX = "/topic/room-stage/";

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyRoomStageChanged(String roomId, Stage stage) {
        RoomStageChangedPayload payload = new RoomStageChangedPayload(stage);
        sendToWaitingRoom(roomId, payload);
    }

    private void sendToWaitingRoom(String roomId, RoomStageChangedPayload payload) {
        String waitingRoomTopic = getRoomStageTopic(roomId);
        messagingTemplate.convertAndSend(waitingRoomTopic, payload);
    }

    private String getRoomStageTopic(String roomId) {
        return ROOM_STAGE_TOPIC_PREFIX + roomId;
    }
}
