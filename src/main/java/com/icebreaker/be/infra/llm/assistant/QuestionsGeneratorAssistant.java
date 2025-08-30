package com.icebreaker.be.infra.llm.assistant;

public interface QuestionsGeneratorAssistant {

    Questions generate(String text);
}
