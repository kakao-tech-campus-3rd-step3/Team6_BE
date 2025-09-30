package com.icebreaker.be.application.room.statemachine.action;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.common.resolver.Resolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStageActionHandlerResolver implements Resolver<Stage, RoomStageActionHandler> {

    private final RoomStageActionHandlerRegistry registry;

    @Override
    public boolean supports(Stage stage) {
        return registry.contains(stage);
    }

    @Override
    public RoomStageActionHandler resolve(Stage stage) {
        if (!supports(stage)) {
            throw new IllegalArgumentException("해당 Stage에 대한 Action이 없습니다: " + stage);
        }
        return registry.getHandler(stage);
    }
}
