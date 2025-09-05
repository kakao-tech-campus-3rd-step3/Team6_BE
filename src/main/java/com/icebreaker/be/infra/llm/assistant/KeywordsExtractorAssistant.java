package com.icebreaker.be.infra.llm.assistant;

public interface KeywordsExtractorAssistant {

    Keywords extract(String text);
}
