package com.icebreaker.be.application.game;

import com.icebreaker.be.application.game.handler.GameHandler;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.common.resolver.Resolver;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameManager {

    private final Resolver<GameCategory, GameHandler> gameActionResolver;

    private final RoomStageRepository roomStageRepository;


    public void startGame(String roomCode) {
        GameHandler gameHandler = gameActionResolver.resolve(resolveCategory(roomCode));
        gameHandler.handle(roomCode);
    }


    private GameCategory resolveCategory(String roomCode) {
        Stage currentStage = roomStageRepository.findByRoomCode(roomCode)
                .map(RoomStage::stage)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_STAGE_NOT_FOUND));

        return GameCategory.fromStage(currentStage);
    }
}
