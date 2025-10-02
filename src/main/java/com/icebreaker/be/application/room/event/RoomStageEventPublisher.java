package com.icebreaker.be.application.room.event;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.room.vo.StageEventType;

public interface RoomStageEventPublisher {

    void publishStageInitialized(String roomCode);

    void publishStageChanged(String roomCode, StageEventType eventType, Stage stage);
}