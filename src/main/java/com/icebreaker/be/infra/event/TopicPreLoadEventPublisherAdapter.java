package com.icebreaker.be.infra.event;

import com.icebreaker.be.application.topic.event.TopicPreLoadEvent;
import com.icebreaker.be.application.topic.event.TopicPreLoadEventPublisher;
import com.icebreaker.be.domain.room.vo.RoomParticipantInterest;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TopicPreLoadEventPublisherAdapter extends EventPublisherSupport implements
        TopicPreLoadEventPublisher {

    public TopicPreLoadEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    @Override
    public void publishInitialized(String roomCode,
            List<RoomParticipantInterest> roomParticipantInterests) {
        publishEvent(new TopicPreLoadEvent(roomCode, roomParticipantInterests));
    }
}
