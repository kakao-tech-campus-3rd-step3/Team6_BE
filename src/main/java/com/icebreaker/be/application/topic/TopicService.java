package com.icebreaker.be.application.topic;

import com.icebreaker.be.application.topic.event.TopicPreLoadEventPublisher;
import com.icebreaker.be.domain.room.repo.RoomParticipantRepository;
import com.icebreaker.be.domain.topic.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final RoomParticipantRepository roomRepository;

    private final TopicRepository topicRepository;
    private final TopicPreLoadEventPublisher topicPreLoadEventPublisher;

    @Transactional
    public void preloadTopicsByRoomCode(String roomCode) {
        var interests = roomRepository.findUserWithInterestsByRoomCode(roomCode);

        topicPreLoadEventPublisher.publishInitialized(roomCode, interests);
    }

    public boolean isTopicPreloaded(String roomCode) {
        return topicRepository.isInitialized(roomCode);
    }
}
