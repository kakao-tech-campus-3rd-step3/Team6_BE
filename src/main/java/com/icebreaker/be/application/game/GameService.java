package com.icebreaker.be.application.game;

import com.icebreaker.be.application.game.dto.GameContext;
import com.icebreaker.be.application.room.RoomOwnerService;
import com.icebreaker.be.application.topic.TopicService;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomOwnerService roomOwnerService;
    private final TopicService topicService;
    private final GameManager gameManager;

    public void start(GameContext gameContext, Long userId) {
        String roomCode = gameContext.getRoomCode();
//        roomOwnerService.validateRoomOwner(roomCode, userId);
        if (!topicService.isTopicPreloaded(roomCode)) {
            throw new BusinessException(ErrorCode.TOPIC_NOT_PRELOADED);
        }
        gameManager.startGame(gameContext);
    }

    public void sendGameList(String roomCode, Long userId) {
//        roomOwnerService.validateRoomOwner(roomCode, userId);
        gameManager.sendGameList(roomCode);
    }
}
