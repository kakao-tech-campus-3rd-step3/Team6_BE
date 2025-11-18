package com.icebreaker.be.infra.scheduler;

import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.domain.user.Interest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionScheduler {

    private final QuestionAsyncExecutor asyncExecutor;

    @Scheduled(cron = "${scheduler.question.cron}", zone = "${scheduler.question.timezone}")
    public void run() {
        log.info("[QuestionScheduler] 질문 생성 스케줄 시작");

        var allTopics = Interest.getAllDisplayNames().stream()
                .map(Topic::new)
                .toList();

        for (Topic topic : allTopics) {
            asyncExecutor.processTopicAsync(topic);
        }
    }
}