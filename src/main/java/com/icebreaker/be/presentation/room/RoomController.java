package com.icebreaker.be.presentation.room;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @MessageMapping("/room/{roomCode}/change-stage")
    public void handleRoomStageChange(
            @DestinationVariable String roomCode,
            @Payload ChangeRoomStageCommand command,
            @CurrentUser Long userId
    ) {
        roomService.changeRoomStage(roomCode, userId, command);
    }

    @MessageMapping("/room/{roomCode}/participants")
    public void handleRoomParticipants(
            @DestinationVariable String roomCode,
            @CurrentUser Long userId
    ) {
        roomService.sendRoomParticipants(roomCode, userId);
    }
}