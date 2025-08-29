package com.icebreaker.be.infra.llm.config;

import com.icebreaker.be.infra.llm.assisstant.KeywordsExtractorAssistant;
import com.icebreaker.be.infra.llm.assisstant.QuestionsGeneratorAssistant;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String modelName;

    @Bean
    public ChatModel chatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .responseFormat(ResponseFormat.JSON)
                .modelName(modelName)
                .build();
    }


    @Bean
    public KeywordsExtractorAssistant keywordsExtractorAssistant(ChatModel chatModel) {
        return AiServices.builder(KeywordsExtractorAssistant.class)
                .chatModel(chatModel)
                .build();
    }

    @Bean
    public QuestionsGeneratorAssistant questionsGeneratorAssistant(ChatModel chatModel) {
        return AiServices.builder(QuestionsGeneratorAssistant.class)
                .chatModel(chatModel)
                .build();
    }

}
