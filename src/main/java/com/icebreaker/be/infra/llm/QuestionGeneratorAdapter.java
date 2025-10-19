package com.icebreaker.be.infra.llm;

import com.icebreaker.be.application.question.llm.QuestionGenerator;
import com.icebreaker.be.domain.question.Question;
import com.icebreaker.be.domain.topic.Topic;
import com.icebreaker.be.infra.llm.assistant.QuestionsGeneratorAssistant;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionGeneratorAdapter implements QuestionGenerator {

    private final QuestionsGeneratorAssistant assistant;

    @Override
    public List<Question> generateQuestions(Set<Topic> topics) {
        return assistant.generate(topics.toString() + "공적").values()
                .stream()
                .map(Question::new)
                .toList();
    }
}
