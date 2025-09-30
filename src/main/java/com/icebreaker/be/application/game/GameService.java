package com.icebreaker.be.application.game;

import com.icebreaker.be.application.game.handler.GameHandler;
import com.icebreaker.be.application.game.messaging.GameResultNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.resolver.Resolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final Resolver<GameCategory, GameHandler> gameActionResolver;
    private final GameResultNotifier notifier;

    public void start(String roomCode, GameCategory category) {
        GameHandler gameHandler = gameActionResolver.resolve(category);
        GameResult result = gameHandler.handle();

        notifier.notifyGameResult(roomCode, result);
    }
}


