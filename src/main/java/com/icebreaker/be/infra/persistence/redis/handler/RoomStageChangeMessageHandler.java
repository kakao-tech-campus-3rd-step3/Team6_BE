package com.icebreaker.be.infra.persistence.redis.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.messaging.room.RoomStageWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessageType;
import com.icebreaker.be.infra.persistence.redis.message.RoomStageChangeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStageChangeMessageHandler implements MessageHandler {

    private final RoomStageWebSocketNotifier roomStageWebSocketNotifier;
    private final ObjectMapper objectMapper;

    @Override
    public PubSubMessageType getMessageType() {
        return PubSubMessageType.ROOM_STAGE_CHANGE;
    }

    @Override
    public void handleAndSend(Object payload) {
        RoomStageChangeMessage stageChangePayload = objectMapper.convertValue(payload,
                RoomStageChangeMessage.class);
        roomStageWebSocketNotifier.notifyRoomStageChanged(
                stageChangePayload.getRoomCode(), stageChangePayload.getStage());
    }
}
