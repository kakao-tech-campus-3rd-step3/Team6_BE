package com.icebreaker.be.infra.llm.embedding;

import com.icebreaker.be.application.question.llm.QuestionStore;
import com.icebreaker.be.domain.question.Question;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionEmbeddingStore implements QuestionStore {

    private static final double SIMILARITY_THRESHOLD = 0.719; // 경험적 설정 하이퍼파라미터 값
    private static final double EQUALITY_THRESHOLD = 1.0;

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    public void saveAllEmbeddings(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            log.debug("[SKIP] 저장할 질문이 없습니다.");
            return;
        }

        // 1. 입력 질문을 TextSegment로 변환하고 완전 중복 제거
        List<TextSegment> segments = questions.stream()
                .map(q -> TextSegment.from(q.content()))
                .distinct()
                .toList();

        // 2️. 한 번에 임베딩 생성 (batch embedding)
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 3️. 중복 제거 후 저장할 Map 구성
        Map<TextSegment, Embedding> newEmbeddings = new LinkedHashMap<>();

        for (int i = 0; i < embeddings.size(); i++) {
            TextSegment segment = segments.get(i);
            Embedding embedding = embeddings.get(i);

            if (existsSimilarQuestions(segments.get(i).text())) {
                log.debug("[SKIP] 유사한 질문이 이미 존재합니다. text={}", segment.text());
                continue;
            }
            newEmbeddings.put(segment, embedding);
        }

        if (newEmbeddings.isEmpty()) {
            log.debug("[SKIP] 모든 질문이 중복되어 저장되지 않았습니다.");
            return;
        }

        embeddingStore.addAll(
                newEmbeddings.values().stream().toList(),
                newEmbeddings.keySet().stream().toList()
        );

        log.info("[SAVE] {}개의 질문이 새로 저장되었습니다.", newEmbeddings.size());
    }


    public List<Question> getSimilarQuestions(String query, int topK) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        var searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .build();

        return embeddingStore.search(searchRequest)
                .matches()
                .stream()
                .filter(m -> m.score() > SIMILARITY_THRESHOLD)
                .map(m -> new Question(m.embedded().text()))
                .toList();
    }

    public void resetStore() {
        embeddingStore.removeAll();
        log.info("Question Embedding Store has been reset.");
    }

    private boolean existsSimilarQuestions(String query) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        var searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();

        return embeddingStore.search(searchRequest)
                .matches()
                .stream()
                .anyMatch(m -> m.score() >= EQUALITY_THRESHOLD);
    }
}
