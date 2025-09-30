package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.Stage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RoomStageActionHandlerRegistry {

    private final Map<Stage, RoomStageActionHandler> handlerMap;

    public RoomStageActionHandlerRegistry(List<RoomStageActionHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(RoomStageActionHandler::getStage, h -> h));
    }

    public boolean contains(Stage stage) {
        return handlerMap.containsKey(stage);
    }

    public RoomStageActionHandler getHandler(Stage stage) {
        return handlerMap.get(stage);
    }
}