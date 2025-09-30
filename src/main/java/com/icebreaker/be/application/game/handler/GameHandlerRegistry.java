package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.util.CollectorsUtils;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GameHandlerRegistry {

    private final Map<GameCategory, GameHandler> actionMap;

    public GameHandlerRegistry(List<GameHandler> actions) {
        this.actionMap = actions.stream()
                .collect(CollectorsUtils.toMapByKey(GameHandler::getCategory));
    }

    public GameHandler getHandler(GameCategory category) {
        GameHandler action = actionMap.get(category);
        if (action == null) {
            throw new IllegalArgumentException("해당 category 대한 Action이 없습니다: " + category);
        }
        return action;
    }

    public boolean contains(GameCategory category) {
        return actionMap.containsKey(category);
    }
}
