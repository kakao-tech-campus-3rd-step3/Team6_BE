package com.icebreaker.be.application.game.messaging;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.UnicastGameResult;
import com.icebreaker.be.domain.game.GameCategory;
import java.util.List;

public interface GameNotifier {

    /**
     * communication based unicast
     */
    void notifyGameResultToUser(UnicastGameResult<?> gameResult);

    /**
     * communication based broadCast
     */
    void notifyGameResult(String roomCode, BroadcastGameResult<?> gameResult);

    /**
     * communication based broadCast
     */
    void notifyGameList(String roomCode, List<GameCategory> categories);
}
