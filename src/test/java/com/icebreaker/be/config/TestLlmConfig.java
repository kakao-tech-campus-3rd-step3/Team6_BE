package com.icebreaker.be.config;

import com.icebreaker.be.infra.llm.assistant.QuestionsGeneratorAssistant;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestLlmConfig {

    @Bean
    @Primary
    public QuestionsGeneratorAssistant questionsGeneratorAssistant() {
        return Mockito.mock(QuestionsGeneratorAssistant.class);
    }
}
