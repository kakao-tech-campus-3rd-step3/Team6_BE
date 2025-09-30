package com.icebreaker.be.application.game.messaging;

import com.icebreaker.be.application.game.GameResult;

public interface GameResultNotifier {

    void notifyGameResult(String roomId, GameResult gameResult);
}
