package com.icebreaker.be.presentation.game;

import com.icebreaker.be.application.game.GameService;
import com.icebreaker.be.application.game.dto.GameContext;
import com.icebreaker.be.global.annotation.CurrentUser;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @MessageMapping("/room/{roomCode}/start-game")
    public void handleGameStarted(
            @DestinationVariable String roomCode,
            @Payload GameContext gameContext,
            @CurrentUser Long userId
    ) {
        if (!roomCode.equals(gameContext.getRoomCode())) {
            throw new BusinessException(ErrorCode.INVALID_ROOM_CODE);
        }
        gameService.start(gameContext, userId);
    }

    @MessageMapping("/room/{roomCode}/game-list")
    public void handleGameList(
            @DestinationVariable String roomCode,
            @CurrentUser Long userId
    ) {
        gameService.sendGameList(roomCode, userId);
    }
}
