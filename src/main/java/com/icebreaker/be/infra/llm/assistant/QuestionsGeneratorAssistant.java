package com.icebreaker.be.infra.llm.assistant;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface QuestionsGeneratorAssistant {

    @SystemMessage(fromResource = "/prompts/question-generator.md")
    Questions generate(@UserMessage String text);
}
