package com.icebreaker.be.application.game.messaging;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.domain.game.GameCategory;
import java.util.List;

public interface GameResultNotifier {

    void notifyGameResult(String roomCode, GameResult gameResult);

    void notifyGameList(String roomCode, List<GameCategory> categories);
}
