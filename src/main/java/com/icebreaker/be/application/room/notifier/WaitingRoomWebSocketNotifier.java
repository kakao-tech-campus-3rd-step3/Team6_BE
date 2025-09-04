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

    public void notifyParticipantJoined(String roomId,
            WaitingRoomParticipant newParticipant) {

        ParticipantJoinedMessage message = new ParticipantJoinedMessage(
                WaitingRoomMessageType.PARTICIPANT_JOINED,
                newParticipant
        );

        messagingTemplate.convertAndSend(
                getWaitingRoomTopic(roomId),
                message
        );
    }

    public void notifyRoomStarted(String roomId) {
        RoomStartedMessage message = new RoomStartedMessage(
                WaitingRoomMessageType.ROOM_STARTED,
                roomId
        );

        messagingTemplate.convertAndSend(
                getWaitingRoomTopic(roomId),
                message
        );
    }

    private String getWaitingRoomTopic(String roomId) {
        return WAITING_ROOM_TOPIC_PREFIX + roomId;
    }


}
