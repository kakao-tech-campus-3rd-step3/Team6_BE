package com.icebreaker.be.infra.llm.assisstant;

public interface QuestionsGeneratorAssistant {

    Questions generate(String text);
}
