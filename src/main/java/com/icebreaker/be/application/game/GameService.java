package com.icebreaker.be.application.game;

import com.icebreaker.be.application.game.handler.GameHandler;
import com.icebreaker.be.application.game.messaging.GameResultNotifier;
import com.icebreaker.be.application.room.RoomOwnerService;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import com.icebreaker.be.domain.room.vo.RoomStage;
import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.common.resolver.Resolver;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final Resolver<GameCategory, GameHandler> gameActionResolver;

    private final RoomOwnerService roomOwnerService;
    private final RoomStageRepository roomStageRepository;

    private final GameResultNotifier notifier;

    public void start(String roomCode, Long userId) {
        roomOwnerService.validateRoomOwner(roomCode, userId);

        GameCategory category = resolveCategory(roomCode);
        GameHandler gameHandler = gameActionResolver.resolve(category);
        GameResult result = gameHandler.handle();

        notifier.notifyGameResult(roomCode, result);
    }

    public void sendGameList(String roomCode, Long userId) {
        roomOwnerService.validateRoomOwner(roomCode, userId);

        List<GameCategory> categories = List.of(GameCategory.values());
        notifier.notifyGameList(roomCode, categories);
    }

    private GameCategory resolveCategory(String roomCode) {
        Stage currentStage = roomStageRepository.findByRoomCode(roomCode)
                .map(RoomStage::stage)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        try {
            return GameCategory.fromStage(currentStage);
        } catch (IllegalArgumentException e) {
            log.warn("RoomStage가 게임이 아님: roomCode={}, stage={}", roomCode, currentStage);
            throw new BusinessException(ErrorCode.INVALID_ROOM_STAGE);
        }
    }
}


