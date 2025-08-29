package com.icebreaker.be.infra.llm.assisstant;

public interface KeywordsExtractorAssistant {

    Keywords extract(String text);
}
