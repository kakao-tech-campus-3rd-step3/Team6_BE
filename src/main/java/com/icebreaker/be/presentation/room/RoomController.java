package com.icebreaker.be.presentation.room;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @MessageMapping("/room/{roomId}/change-stage")
    public void changeRoomStage(
            @DestinationVariable String roomId,
            @Payload ChangeRoomStageCommand command
    ) {
        roomService.changeStage(roomId, command);
    }
}
