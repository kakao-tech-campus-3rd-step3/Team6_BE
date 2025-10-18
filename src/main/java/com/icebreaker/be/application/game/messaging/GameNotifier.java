package com.icebreaker.be.application.game.messaging;

import com.icebreaker.be.application.game.handler.GameResult;
import com.icebreaker.be.domain.game.GameCategory;
import java.util.List;

public interface GameNotifier {

    /**
     * communication based unicast
     */
    void notifyGameResultToUser(String userId, GameResult<?> gameResult);

    /**
     * communication based broadCast
     */
    void notifyGameResult(String roomCode, GameResult<?> gameResult);

    /**
     * communication based broadCast
     */
    void notifyGameList(String roomCode, List<GameCategory> categories);
}
