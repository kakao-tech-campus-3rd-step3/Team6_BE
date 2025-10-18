package com.icebreaker.be.infra.messaging.game;

import com.icebreaker.be.application.game.handler.GameResult;
import com.icebreaker.be.application.game.messaging.GameNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.infra.messaging.AbstractStompNotifier;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GameResultWebsocketNotifier extends AbstractStompNotifier implements
        GameNotifier {

    public GameResultWebsocketNotifier(SimpMessagingTemplate messagingTemplate) {
        super(messagingTemplate);
    }

    @Async
    @Override
    public void notifyGameResultToUser(String userId, GameResult gameResult) {
        sendToUser(userId, "/queue/game-result", gameResult,
                "게임 결과를 정상적으로 전송했습니다.");
    }

    @Override
    public void notifyGameResult(String roomCode, GameResult<?> gameResult) {

    }

    @Async
    @Override
    public void notifyGameList(String roomCode, List<GameCategory> categories) {
        //TODO: WebSocket으로 게임 리스트 전송
    }
}
