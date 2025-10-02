package com.icebreaker.be.infra.messaging.game;

import com.icebreaker.be.application.game.GameResult;
import com.icebreaker.be.application.game.messaging.GameResultNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GameResultWebsocketNotifier implements GameResultNotifier {

    @Override
    public void notifyGameResult(String roomCode, GameResult gameResult) {
        //TODO: WebSocket으로 게임 결과 전송
    }

    @Override
    public void notifyGameList(String roomCode, List<GameCategory> categories) {
        //TODO: WebSocket으로 게임 리스트 전송
    }
}
