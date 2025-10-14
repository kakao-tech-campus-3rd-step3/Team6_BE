package com.icebreaker.be.infra.messaging.room;

import com.icebreaker.be.application.room.messaging.RoomStageNotifier;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.infra.messaging.AbstractStompNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomStageWebSocketNotifier extends AbstractStompNotifier implements RoomStageNotifier {

    private static final String ROOM_STAGE_TOPIC_PREFIX = "/topic/room-stage/";

    public RoomStageWebSocketNotifier(SimpMessagingTemplate messagingTemplate) {
        super(messagingTemplate);
    }

    @Override
    public void notifyRoomStageChanged(String roomId, Stage stage) {
        String topic = buildTopic(ROOM_STAGE_TOPIC_PREFIX, roomId);
        send(topic, new RoomStageChangedPayload(stage), "룸 스테이지가 변경되었습니다.");

        log.debug("Notified room stage change to topic {}: new stage {}", topic, stage);
    }
}
