package com.icebreaker.be.infra.scheduler;

import com.icebreaker.be.application.question.QuestionPoolService;
import com.icebreaker.be.domain.topic.Topic;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionAsyncExecutor {

    private static final long LOCK_WAIT_TIME = 1L;
    private static final long LOCK_LEASE_TIME = 10L;
    private static final TimeUnit LOCK_TIME_UNIT = TimeUnit.MINUTES;

    private static final String INIT_FLAG_KEY = "question-pool:initialized";
    private static final String LOCK_PREFIX = "generate-question-lock:";

    private final RedissonClient redissonClient;
    private final QuestionPoolService questionPoolService;

    @Async("topicExecutor")
    public void processTopicAsync(Topic topic) {
        String topicName = topic.name();
        String lockKey = LOCK_PREFIX + topicName;

        try {
            if (!tryExecuteWithLock(lockKey, () -> executeTopicProcess(topic))) {
                log.info("[{}] 이미 다른 인스턴스가 처리 중입니다. skip.", topicName);
            }
        } catch (Exception e) {
            log.error("[{}] 처리 중 예외 발생", topicName, e);
        }
    }

    private boolean tryExecuteWithLock(String lockKey, Runnable task) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, LOCK_TIME_UNIT);
            if (!locked) {
                return false;
            }
            task.run();
            return true;
        } finally {
            if (locked) {
                lock.unlock();
                log.debug("[{}] 락 해제 완료", lockKey);
            }
        }
    }

    private void executeTopicProcess(Topic topic) {
        String topicName = topic.name();
        log.info("[{}] 질문 생성 시작", topicName);

        if (shouldInitializePool()) {
            initializeQuestionPool(topic);
        } else {
            log.debug("[{}] 질문 풀 초기화 이미 완료, skip", topicName);
        }

        questionPoolService.buildQuestionPoolFor(topic);
        log.info("[{}] 질문 생성 완료", topicName);
    }


    private boolean shouldInitializePool() {
        var initializedFlag = redissonClient.getBucket(INIT_FLAG_KEY);
        return initializedFlag.get() == null;
    }

    private void initializeQuestionPool(Topic topic) {
        questionPoolService.clearOldQuestionPool(topic);
        redissonClient.getBucket(INIT_FLAG_KEY).set(true);
        log.info("[{}] 질문 풀 초기화 완료 (클러스터 전체 1회 실행)", topic.name());
    }
}
