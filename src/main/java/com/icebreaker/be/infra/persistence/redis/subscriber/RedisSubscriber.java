package com.icebreaker.be.infra.persistence.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.messaging.room.RoomStageWebSocketNotifier;
import com.icebreaker.be.infra.messaging.waitingroom.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.ParticipantJoinedMessage;
import com.icebreaker.be.infra.persistence.redis.message.PubSubMessage;
import com.icebreaker.be.infra.persistence.redis.message.RoomStageChangeMessage;
import com.icebreaker.be.infra.persistence.redis.message.RoomStartedMessage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final WaitingRoomWebSocketNotifier waitingRoomWebSocketNotifier;
    private final RoomStageWebSocketNotifier roomStageWebSocketNotifier;
    private final ObjectMapper objectMapper;

    //Redis 채널로 들어온 메시지를 받아서, 최종적으로 사용자에게 보내야함
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = new String(message.getBody());
            PubSubMessage<?> pubSubMessage = objectMapper.readValue(jsonMessage,
                    PubSubMessage.class);
            log.info("Received redis message: {}", jsonMessage);
            switch (pubSubMessage.getType()) {
                case PARTICIPANT_JOINED -> {
                    ParticipantJoinedMessage joinedPayload = objectMapper.convertValue(
                            pubSubMessage.getMessage(), ParticipantJoinedMessage.class);
                    waitingRoomWebSocketNotifier.notifyParticipantJoined(
                            joinedPayload.getRoomId(),
                            joinedPayload.getWaitingRoomWithParticipants());
                }
                case ROOM_STARTED -> {
                    RoomStartedMessage startedPayload = objectMapper.convertValue(
                            pubSubMessage.getMessage(), RoomStartedMessage.class);
                    waitingRoomWebSocketNotifier.notifyRoomStarted(startedPayload.getRoomId());
                }
                case ROOM_STAGE_CHANGE -> {
                    RoomStageChangeMessage stageChangePayload = objectMapper.convertValue(
                            pubSubMessage.getMessage(), RoomStageChangeMessage.class);
                    roomStageWebSocketNotifier.notifyRoomStageChanged(
                            stageChangePayload.getRoomCode(), stageChangePayload.getStage());
                }
                default -> log.info("Received unknown message type");
            }
            log.info("Successfully processed message for type: {}", pubSubMessage.getType());
        } catch (IOException e) {
            log.error("Failed to parse Redis message", e);
        }

    }
}
