package com.icebreaker.be.application.game.handler;

import com.icebreaker.be.application.game.dto.GameResult;
import com.icebreaker.be.application.game.dto.ManittoGameContext;
import com.icebreaker.be.application.game.dto.UnicastGameResult;
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
public class ManittoGameHandler implements GameHandler<ManittoGameContext> {

    private final RoomParticipantRepository roomParticipantRepository;

    @Override
    public GameCategory getCategory() {
        return GameCategory.MANITTO;
    }

    @Override
    public GameResult handle(ManittoGameContext ctx) {
        String roomCode = ctx.getRoomCode();

        List<User> participants = roomParticipantRepository.findUsersByRoomCode(roomCode);
        Map<User, User> pairs = MatchingUtils.generateDerangementPairs(participants);

        List<UnicastGameResult.UserResult<User>> userResults = pairs.entrySet().stream()
                .map(entry -> new UnicastGameResult.UserResult<>(
                        entry.getKey().getId().toString(),
                        entry.getValue()
                )).toList();

        return UnicastGameResult.of(userResults);
    }
}
