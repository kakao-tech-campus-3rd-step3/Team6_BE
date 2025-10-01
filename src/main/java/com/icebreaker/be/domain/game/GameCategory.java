package com.icebreaker.be.domain.game;

import com.icebreaker.be.domain.room.vo.Stage;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.EnumMap;
import java.util.Map;

public enum GameCategory {
    MANITTO,
    RANDOM_ROULETTE,
    TOPIC_RECOMMEND;

    private static final Map<Stage, GameCategory> stageToGameCategory = new EnumMap<>(Stage.class);

    static {
        stageToGameCategory.put(Stage.MANITTO_STAGE, MANITTO);
        stageToGameCategory.put(Stage.RANDOM_ROULETTE_STAGE, RANDOM_ROULETTE);
        stageToGameCategory.put(Stage.TOPIC_RECOMMEND_STAGE, TOPIC_RECOMMEND);
    }

    public static GameCategory fromStage(Stage stage) {
        GameCategory category = stageToGameCategory.get(stage);
        if (category == null) {
            throw new BusinessException(ErrorCode.INVALID_GAME_CATEGORY);
        }
        return category;
    }
}
