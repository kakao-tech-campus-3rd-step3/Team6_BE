package com.icebreaker.be.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {

    private static final int LLM_CORE = 10;
    private static final int LLM_MAX = 30;
    private static final int LLM_QUEUE = 100;
    private static final String LLM_PREFIX = "LLM-";

    private static final int TOPIC_CORE = 5;
    private static final int TOPIC_MAX = 10;
    private static final int TOPIC_QUEUE = 100;
    private static final String TOPIC_PREFIX = "topic-async-";

    @Bean(name = "llmExecutor")
    public Executor llmExecutor() {
        return createExecutor(LLM_CORE, LLM_MAX, LLM_QUEUE, LLM_PREFIX);
    }

    @Bean(name = "topicExecutor")
    public ThreadPoolTaskExecutor topicExecutor() {
        return createExecutor(TOPIC_CORE, TOPIC_MAX, TOPIC_QUEUE, TOPIC_PREFIX);
    }

    private ThreadPoolTaskExecutor createExecutor(int core, int max, int queue, String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(max);
        executor.setQueueCapacity(queue);
        executor.setThreadNamePrefix(prefix);
        executor.initialize();
        return executor;
    }
}