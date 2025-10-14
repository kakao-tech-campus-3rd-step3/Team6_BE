package com.icebreaker.be.infra.messaging.room;

import com.icebreaker.be.application.room.dto.RoomParticipantCommand;
import com.icebreaker.be.application.room.messaging.RoomNotifier;
import com.icebreaker.be.infra.messaging.AbstractStompNotifier;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomWebSocketNotifier extends AbstractStompNotifier implements RoomNotifier {

    private static final String ROOM_PARTICIPANT_TOPIC_PREFIX = "/topic/room-participant/";

    public RoomWebSocketNotifier(SimpMessagingTemplate messagingTemplate) {
        super(messagingTemplate);
    }

    @Override
    public void notifyRoomParticipants(String roomId, List<RoomParticipantCommand> participants) {
        String topic = buildTopic(ROOM_PARTICIPANT_TOPIC_PREFIX, roomId);
        send(topic, participants, "룸 참여자 목록을 정상적으로 전송했습니다.");

        log.debug("Notified room participants to topic: {}", topic);
    }
}
