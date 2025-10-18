package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.messaging.GameNotifier;
import com.icebreaker.be.application.game.util.MatchingUtils;
import com.icebreaker.be.domain.game.GameCategory;
import com.icebreaker.be.domain.room.repo.RoomParticipantRepository;
import com.icebreaker.be.domain.user.User;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManittoGameHandler implements GameHandler {

    private final RoomParticipantRepository roomParticipantRepository;
    private final GameNotifier notifier;

    @Override
    public GameCategory getCategory() {
        return GameCategory.MANITTO;
    }

    @Override
    public void handle(String roomCode) {
        List<User> participants = roomParticipantRepository.findUsersByRoomCode(roomCode);
        Map<User, User> pairs = MatchingUtils.generateDerangementPairs(participants);
        pairs.keySet().parallelStream().forEach((giver) -> {
            User receiver = pairs.get(giver);
            notifier.notifyGameResultToUser(
                    receiver.getId().toString(),
                    new GameResult<>(getCategory(), giver)
            );
        });
    }
}
