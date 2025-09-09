package com.icebreaker.be.application.room.notify;

import com.icebreaker.be.domain.room.entity.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomStageWebSocketNotifier {

    private static final String ROOM_STAGE_TOPIC_PREFIX = "/topic/room-stage/";

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyRoomStageChanged(String roomId, Stage stage) {
        RoomStageChangedPayload payload = new RoomStageChangedPayload(stage);
        sendToRoomStageTopic(roomId, payload);
    }

    private void sendToRoomStageTopic(String roomId, RoomStageChangedPayload payload) {
        String waitingRoomTopic = getRoomStageTopic(roomId);
        messagingTemplate.convertAndSend(waitingRoomTopic, payload);
    }

    private String getRoomStageTopic(String roomId) {
        return ROOM_STAGE_TOPIC_PREFIX + roomId;
    }
}
