package com.icebreaker.be.application.game;

import static com.icebreaker.be.domain.game.GameCategory.ALL_GAME_CATEGORIES;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.GameContext;
import com.icebreaker.be.application.game.dto.GameResult;
import com.icebreaker.be.application.game.dto.UnicastGameResult;
import com.icebreaker.be.application.game.handler.GameHandler;
import com.icebreaker.be.application.game.handler.GameHandlerRegistry;
import com.icebreaker.be.application.game.messaging.GameNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameManager {

    private final GameHandlerRegistry gameHandlerRegistry;
    private final GameNotifier notifier;

    private final RoomStageRepository roomStageRepository;


    public void startGame(GameContext ctx) {
        String roomCode = ctx.getRoomCode();
        GameHandler<GameContext> gameHandler = gameHandler(roomCode);
        GameResult gameResult = gameHandler.handle(ctx);

        if (gameResult instanceof BroadcastGameResult<?> broadcastResult) {
            notifier.notifyGameResult(ctx.getRoomCode(), broadcastResult);
        } else if (gameResult instanceof UnicastGameResult<?> unicastResult) {
            notifier.notifyGameResultToUser(unicastResult);
        }
    }

    public void sendGameList(String roomCode) {
        notifier.notifyGameList(roomCode, ALL_GAME_CATEGORIES);
    }

    @SuppressWarnings("unchecked")
    private GameHandler<GameContext> gameHandler(String roomCode) {
        GameCategory category = resolveCategory(roomCode);
        return (GameHandler<GameContext>) gameHandlerRegistry.getHandler(category);
    }

    private GameCategory resolveCategory(String roomCode) {
        Stage currentStage = roomStageRepository.findByRoomCode(roomCode)
                .map(RoomStage::stage)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_STAGE_NOT_FOUND));

        return GameCategory.fromStage(currentStage);
    }
}
