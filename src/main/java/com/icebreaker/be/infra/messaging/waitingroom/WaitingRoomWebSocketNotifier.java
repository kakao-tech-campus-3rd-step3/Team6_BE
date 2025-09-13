package com.icebreaker.be.infra.messaging.waitingroom;

import com.icebreaker.be.application.waitingroom.messaging.WaitingRoomNotifier;
import com.icebreaker.be.domain.waitingroom.WaitingRoomWithParticipants;
import com.icebreaker.be.infra.messaging.AbstractStompNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WaitingRoomWebSocketNotifier extends AbstractStompNotifier
        implements WaitingRoomNotifier {

    private static final String WAITING_ROOM_TOPIC_PREFIX = "/topic/waiting-room/";

    public WaitingRoomWebSocketNotifier(SimpMessagingTemplate messagingTemplate) {
        super(messagingTemplate);
    }

    @Override
    public void notifyParticipantJoined(
            String roomId,
            WaitingRoomWithParticipants waitingRoomWithParticipants
    ) {
        String topic = buildTopic(WAITING_ROOM_TOPIC_PREFIX, roomId);
        send(topic, new ParticipantJoinedPayload(waitingRoomWithParticipants), "대기방에 참가자가 입장했습니다.");
    }

    @Override
    public void notifyRoomStarted(String roomId) {
        String topic = buildTopic(WAITING_ROOM_TOPIC_PREFIX, roomId);
        send(topic, new RoomStartedPayload(roomId), "대기방이 시작되었습니다.");
    }
}
