package com.icebreaker.be.application.game;

import static com.icebreaker.be.domain.game.GameCategory.ALL_GAME_CATEGORIES;

import com.icebreaker.be.application.game.messaging.GameNotifier;
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
    private final GameNotifier notifier;

    public void start(String roomCode, Long userId) {
        roomOwnerService.validateRoomOwner(roomCode, userId);
        if (!topicService.isTopicPreloaded(roomCode)) {
            throw new BusinessException(ErrorCode.TOPIC_NOT_PRELOADED);
        }
        gameManager.startGame(roomCode);
    }

    public void sendGameList(String roomCode, Long userId) {
        roomOwnerService.validateRoomOwner(roomCode, userId);
        notifier.notifyGameList(roomCode, ALL_GAME_CATEGORIES);
    }
}


