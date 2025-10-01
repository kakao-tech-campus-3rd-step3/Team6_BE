package com.icebreaker.be.application.game;

import com.icebreaker.be.application.game.handler.GameHandler;
import com.icebreaker.be.application.game.messaging.GameResultNotifier;
import com.icebreaker.be.application.room.RoomService;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.resolver.Resolver;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final Resolver<GameCategory, GameHandler> gameActionResolver;
    private final GameResultNotifier notifier;

    private final RoomService roomService;

    public void start(String roomCode, GameCategory category) {
        GameHandler gameHandler = gameActionResolver.resolve(category);
        GameResult result = gameHandler.handle();

        notifier.notifyGameResult(roomCode, result);
    }

    public void sendGameList(String roomCode, Long userId) {
        roomService.validateRoomOwner(roomCode, userId);
        List<GameCategory> categories = Arrays.asList(GameCategory.values());
        notifier.notifyGameList(roomCode, categories);
    }
}


