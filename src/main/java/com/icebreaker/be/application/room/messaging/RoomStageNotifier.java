package com.icebreaker.be.application.room.messaging;

import com.icebreaker.be.domain.room.vo.Stage;

public interface RoomStageNotifier {

    void notifyRoomStageChanged(String roomId, Stage stage);
}
