package com.icebreaker.be.application.room;

import com.icebreaker.be.domain.room.entity.RoomStage;
import com.icebreaker.be.domain.room.entity.Stage;
import com.icebreaker.be.domain.room.repo.RoomStageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomStageService {

    private final RoomStageRepository roomStageRepository;

    public void initializeStage(String roomCode) {
        RoomStage roomStage = RoomStage.init(roomCode);
        roomStageRepository.save(roomStage);
    }

    public void changeStage(String roomCode, Stage stage) {
        RoomStage roomStage = RoomStage.of(roomCode, stage);
        roomStageRepository.save(roomStage);
    }
}
