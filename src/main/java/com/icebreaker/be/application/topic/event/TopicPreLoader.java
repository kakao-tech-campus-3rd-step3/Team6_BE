package com.icebreaker.be.application.topic.event;

import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.topic.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicPreLoader {

    private final TopicRepository topicRepository;
    private final TopicPreLoadEventPublisher publisher;

    @Async("llmExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void preload(TopicPreLoadEvent event) {

        String roomCode = event.roomCode();

        for (var participant : event.participants()) {
            Long participantId = participant.id();
            List<Topic> participantTopics = participant.toTopics();
            topicRepository.save(roomCode, participantId, participantTopics);
        }
    }
}
