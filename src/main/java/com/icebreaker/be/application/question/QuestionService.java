package com.icebreaker.be.application.question;

import com.icebreaker.be.application.question.dto.CreateQuestionCommand;
import com.icebreaker.be.application.question.dto.QuestionResponseDto;
import com.icebreaker.be.application.question.mapper.QuestionMapper;
import com.icebreaker.be.domain.question.QuestionRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  @Transactional(readOnly = true)
  public QuestionResponseDto getQuestionById(Long id) {
    return questionRepository
        .findById(id)
        .map(QuestionMapper::toResponse)
        .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<QuestionResponseDto> getAllQuestions() {
    return questionRepository
        .findAll()
        .stream()
        .map(QuestionMapper::toResponse)
        .toList();
  }

  @Transactional
  public Long createQuestion(CreateQuestionCommand createQuestionCommand) {
    return questionRepository.save(createQuestionCommand.toEntity()).getId();
  }

  @Transactional
  public void deleteQuestion(Long id) {
    questionRepository.deleteById(id);
  }
}
