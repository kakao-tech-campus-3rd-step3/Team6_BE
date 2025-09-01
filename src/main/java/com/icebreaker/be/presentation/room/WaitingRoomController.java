package com.icebreaker.be.presentation.room;

import com.icebreaker.be.application.room.WaitingRoomService;
import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/waiting-room")
@Slf4j
@RequiredArgsConstructor
public class WaitingRoomController {

    private final WaitingRoomService waitingRoomService;

    @MessageMapping("/{roomId}/join")
    public void joinRoomWebSocket(
            @DestinationVariable String roomId,
            @CurrentUser Long userId
    ) {
        waitingRoomService.joinRoom(roomId, userId);
    }

    @MessageMapping("/create")
    @SendToUser("/queue/waiting-room")
    public String createRoomWebSocket(
            @Payload CreateRoomCommand command,
            @CurrentUser Long userId
    ) {
        log.info("Received request to create a new room {}", userId);
        return waitingRoomService.createRoom(command, userId);
    }
}
