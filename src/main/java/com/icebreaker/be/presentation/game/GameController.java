package com.icebreaker.be.presentation.game;

import com.icebreaker.be.application.game.GameService;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.annotation.CurrentGameCategory;
import com.icebreaker.be.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @MessageMapping("/room/{roomCode}/start-game")
    public void handleGameStarted(
            @DestinationVariable String roomCode,
            @CurrentGameCategory GameCategory category
    ) {
        gameService.start(roomCode, category);
    }

    @MessageMapping("/room/{roomCode}/game-list")
    public void handleGameList(
            @DestinationVariable String roomCode,
            @CurrentUser Long userId
    ) {
        gameService.sendGameList(roomCode, userId);
    }
}
