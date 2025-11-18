package com.icebreaker.be.application.question.llm;

import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.topic.Topic;
import java.util.List;
import java.util.Set;

public interface QuestionGenerator {

    List<Question> generateQuestions(Set<Topic> topics);
}
