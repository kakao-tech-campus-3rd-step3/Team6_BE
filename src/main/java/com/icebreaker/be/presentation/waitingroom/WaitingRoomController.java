package com.icebreaker.be.presentation.waitingroom;

import com.icebreaker.be.application.waitingroom.WaitingRoomService;
import com.icebreaker.be.application.waitingroom.dto.CreateWaitingRoomCommand;
import com.icebreaker.be.application.waitingroom.dto.WaitingRoomId;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class WaitingRoomController {

    private final WaitingRoomService waitingRoomService;

    @MessageMapping("/waiting-room/create")
    @SendToUser("/queue/waiting-room")
    public ApiResponse<WaitingRoomId> createRoom(
            @Payload CreateWaitingRoomCommand command,
            @CurrentUser Long userId
    ) {
        WaitingRoomId roomId = waitingRoomService.createRoom(command, userId);
        return ApiResponseFactory.success(roomId, "대기방이 정상적으로 생성되었습니다.");
    }

    @MessageMapping("/waiting-room/{roomId}/join")
    public void joinRoom(
            @DestinationVariable String roomId,
            @CurrentUser Long userId
    ) {
        waitingRoomService.joinRoom(roomId, userId);
    }
}
