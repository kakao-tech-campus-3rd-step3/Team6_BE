package com.icebreaker.be.infra.messaging.game;

import com.icebreaker.be.application.game.dto.BroadcastGameResult;
import com.icebreaker.be.application.game.dto.UnicastGameResult;
import com.icebreaker.be.application.game.messaging.GameNotifier;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.infra.messaging.AbstractStompNotifier;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GameResultWebsocketNotifier extends AbstractStompNotifier implements
        GameNotifier {

    private static final String GAME_LIST_TOPIC_PREFIX = "/topic/game-list/";
    private static final String GAME_RESULT_TOPIC_PREFIX = "/topic/game-result/";
    private final SimpUserRegistry simpUserRegistry;

    public GameResultWebsocketNotifier(SimpMessagingTemplate messagingTemplate,
            SimpUserRegistry simpUserRegistry) {
        super(messagingTemplate);
        this.simpUserRegistry = simpUserRegistry;
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
                    log.info("Sent game result to userId: {}", unicastGameResult.userId());
                    log.info("Payload: {}", unicastGameResult.payload());
                });
    }

    @Async
    @Override
    public void notifyGameResult(String roomCode, BroadcastGameResult<?> gameResult) {
        String topic = buildTopic(GAME_RESULT_TOPIC_PREFIX, roomCode);
        send(topic, gameResult.payload(), "게임 결과를 정상적으로 전송했습니다.");
        log.info("Sent game result to topic: {}", topic);
    }

    @Async
    @Override
    public void notifyGameList(String roomCode, List<GameCategory> categories) {
        String topic = buildTopic(GAME_LIST_TOPIC_PREFIX, roomCode);
        send(topic, categories, "게임 목록을 정상적으로 전송했습니다.");
        log.info("Sent game categories to topic: {}", categories);
    }
}
