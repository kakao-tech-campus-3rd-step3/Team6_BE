package com.icebreaker.be.infra.persistence.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.infra.messaging.room.RoomStageWebSocketNotifier;
import com.icebreaker.be.infra.messaging.waitingroom.WaitingRoomWebSocketNotifier;
import com.icebreaker.be.infra.persistence.redis.message.ParticipantJoinedMessage;
import com.icebreaker.be.infra.persistence.redis.message.RedisMessage;
import com.icebreaker.be.infra.persistence.redis.message.RedisMessageType;
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
            RedisMessage<?> redisMessage = objectMapper.readValue(jsonMessage,
                    RedisMessage.class);
            log.info("Received redis message: {}", jsonMessage);
            switch (redisMessage.getType()) {
                case RedisMessageType.PARTICIPANT_JOINED:
                    ParticipantJoinedMessage joinedPayload = objectMapper.convertValue(
                            redisMessage.getMessage(), ParticipantJoinedMessage.class);
                    waitingRoomWebSocketNotifier.notifyParticipantJoined(
                            joinedPayload.getRoomId(),
                            joinedPayload.getWaitingRoomWithParticipants());
                    break;

                case RedisMessageType.ROOM_STARTED:
                    RoomStartedMessage startedPayload = objectMapper.convertValue(
                            redisMessage.getMessage(), RoomStartedMessage.class);
                    waitingRoomWebSocketNotifier.notifyRoomStarted(startedPayload.getRoomId());
                    break;

                case RedisMessageType.ROOM_STAGE_CHANGE:
                    RoomStageChangeMessage stageChangePayload = objectMapper.convertValue(
                            redisMessage.getMessage(), RoomStageChangeMessage.class);
                    roomStageWebSocketNotifier.notifyRoomStageChanged(
                            stageChangePayload.getRoomCode(), stageChangePayload.getStage());
                    break;
                default:
                    log.info("Received unknown message type");
            }
            log.info("Successfully processed message for type: {}", redisMessage.getType());
        } catch (IOException e) {
            log.error("Failed to parse Redis message", e);
        }

    }
}
