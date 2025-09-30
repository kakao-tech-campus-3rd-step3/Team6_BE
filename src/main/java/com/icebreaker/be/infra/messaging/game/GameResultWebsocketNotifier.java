package com.icebreaker.be.infra.messaging.game;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.application.game.messaging.GameResultNotifier;
import org.springframework.stereotype.Component;

@Component
public class GameResultWebsocketNotifier implements GameResultNotifier {

    @Override
    public void notifyGameResult(String roomId, GameResult gameResult) {

    }
}
