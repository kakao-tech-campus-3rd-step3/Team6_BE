package com.icebreaker.be.presentation.room;

import com.icebreaker.be.application.room.WaitingRoomService;
import com.icebreaker.be.application.room.dto.CreateRoomCommand;
import com.icebreaker.be.global.annotation.CurrentUser;
import com.icebreaker.be.global.common.response.ApiResponse;
import com.icebreaker.be.global.common.response.ApiResponseFactory;
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

    @MessageMapping("/create")
    @SendToUser("/queue/waiting-room")
    public ApiResponse<String> createRoom(
            @Payload CreateRoomCommand command,
            @CurrentUser Long userId
    ) {
        String roomId = waitingRoomService.createRoom(command, userId);
        return ApiResponseFactory.success(roomId, "대기방이 정상적으로 생성되었습니다.");
    }

    @MessageMapping("/{roomId}/join")
    public void joinRoom(
            @DestinationVariable String roomId,
            @CurrentUser Long userId
    ) {
        waitingRoomService.joinRoom(roomId, userId);
    }
}
