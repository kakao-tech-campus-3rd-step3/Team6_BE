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

    /**
     * 이벤트를 통해서만 접근 가능 RoomCode가 없을 수 없음 (앞에서 기저 처리)
     */
    public void changeStage(String roomCode, Stage stage) {
        RoomStage roomStage = RoomStage.of(roomCode, stage);
        roomStageRepository.save(roomStage);
    }
}
