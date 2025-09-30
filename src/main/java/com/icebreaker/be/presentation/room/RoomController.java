package com.icebreaker.be.presentation.room;

import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.application.room.dto.ChangeRoomStageCommand;
import com.icebreaker.be.application.room.dto.RoomTicket;
import com.icebreaker.be.global.annotation.CurrentUser;
import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //TODO: HOST 만 권한을 가질 수 있음.
    @MessageMapping("/room/{roomCode}/change-stage")
    public void handleRoomStageChange(
            @DestinationVariable String roomCode,
            @Payload ChangeRoomStageCommand command
    ) {
        roomService.changeRoomStage(roomCode, command);
    }

    //TODO: 모두 접근 가능
    @MessageMapping("/room/{roomCode}/request-room-ticket")
    @SendToUser("/queue/room-ticket")
    public SuccessApiResponse<RoomTicket> handleRoomTicketRequest(
            @DestinationVariable String roomCode,
            @CurrentUser Long userId
    ) {
        RoomTicket roomTicket = roomService.issueRoomTicket(roomCode, userId);
        return ApiResponseFactory.success(roomTicket, "룸 티켓이 발급되었습니다.");
    }
}