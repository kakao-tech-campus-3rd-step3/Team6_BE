package com.icebreaker.be.application.question.llm;

import com.icebreaker.be.domain.question.Question;
import java.util.List;

public interface QuestionStore {

    void saveAllEmbeddings(List<Question> questions);

    List<Question> getSimilarQuestions(String query, int topK);

    void resetStore();
}
