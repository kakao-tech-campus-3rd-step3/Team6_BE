package com.icebreaker.be.application.room.messaging;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.domain.user.User;
import java.util.List;

public interface RoomStageNotifier {

    void notifyRoomStageChanged(String roomId, Stage stage);

    void notifyProfileViewStageStarted(String roomCode, List<User> users);
}
