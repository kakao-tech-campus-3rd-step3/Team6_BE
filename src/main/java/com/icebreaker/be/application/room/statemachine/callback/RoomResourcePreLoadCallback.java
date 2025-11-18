package com.icebreaker.be.application.room.statemachine.callback;

import com.icebreaker.be.application.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomResourcePreLoadCallback implements RoomStageInitCallback {

    private final TopicService topicService;

    @Override
    public void onRoomStageInitialized(String roomCode) {
        topicService.preloadTopicsByRoomCode(roomCode);
    }
}
