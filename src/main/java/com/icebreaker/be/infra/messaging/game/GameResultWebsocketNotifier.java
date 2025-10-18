package com.icebreaker.be.infra.messaging.game;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.UnicastGameResult;
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
    public void notifyGameResultToUser(UnicastGameResult<?> gameResult) {
        gameResult.payload().parallelStream()
                .forEach(unicastGameResult -> {
                    sendToUser(
                            unicastGameResult.userId(),
                            "/queue/game-result",
                            unicastGameResult.payload(),
                            "게임 결과를 정상적으로 전송했습니다.");
                });
    }

    @Async
    @Override
    public void notifyGameResult(String roomCode, BroadcastGameResult<?> gameResult) {
        send(roomCode, gameResult.payload(), "게임 결과를 정상적으로 전송했습니다.");
    }

    @Async
    @Override
    public void notifyGameList(String roomCode, List<GameCategory> categories) {
        send(roomCode, categories, "게임 목록을 정상적으로 전송했습니다.");
    }
}
