package com.icebreaker.be.application.interest;

import com.icebreaker.be.application.interest.dto.InterestResponse;
import com.icebreaker.be.application.interest.mapper.InterestMapper;
import com.icebreaker.be.domain.interest.Interest;
import com.icebreaker.be.domain.interest.InterestRepository;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<InterestResponse> getInterestsByIds(List<Long> ids) {
        return interestRepository.findAllById(ids)
                .stream()
                .map(InterestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public InterestResponse getInterestById(Long id) {
        return interestRepository.findById(id)
                .map(interest -> new InterestResponse(interest.getId(), interest.getName()))
                .orElseThrow(() -> new BusinessException(ErrorCode.INTEREST_NOT_FOUND));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<InterestResponse> getAllInterests() {
        return interestRepository.findAll()
                .stream()
                .map(InterestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Long> getInterestIdsByNames(List<String> names) {
        return names.stream()
                .map(this::getInterestIdByName)
                .toList();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Long getInterestIdByName(String name) {
        return interestRepository.findByName(name)
                .map(Interest::getId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTEREST_NOT_FOUND));
    }
}
