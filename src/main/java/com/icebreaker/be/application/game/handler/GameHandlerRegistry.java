package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.global.common.util.CollectorsUtils;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GameHandlerRegistry {

    private final Map<GameCategory, GameHandler> handlerMap;

    public GameHandlerRegistry(List<GameHandler> actions) {
        this.handlerMap = actions.stream()
                .collect(CollectorsUtils.toMapByKey(GameHandler::getCategory));
    }

    public GameHandler getHandler(GameCategory category) {
        GameHandler action = handlerMap.get(category);
        if (action == null) {
            throw new BusinessException(ErrorCode.INVALID_GAME_CATEGORY);
        }
        return action;
    }

    public boolean contains(GameCategory category) {
        return handlerMap.containsKey(category);
    }
}
