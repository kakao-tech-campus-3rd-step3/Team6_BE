package com.icebreaker.be.application.interest;

import com.icebreaker.be.application.interest.dto.InterestResponse;
import com.icebreaker.be.domain.interest.Interest;
import com.icebreaker.be.domain.interest.InterestRepository;
import com.icebreaker.be.fixture.InterestFixture;
import com.icebreaker.be.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterestService 테스트")
class InterestServiceTest {

    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private InterestService interestService;

    @Test
    @DisplayName("ID 목록으로 Interest 목록을 정상적으로 조회한다")
    void getInterestsByIds_Success() {
        // given
        Interest interest1 = InterestFixture.defaultInterest1();
        Interest interest2 = InterestFixture.defaultInterest2();
        ReflectionTestUtils.setField(interest1, "id", 1L);
        ReflectionTestUtils.setField(interest2, "id", 2L);
        List<Long> ids = List.of(1L, 2L);
        given(interestRepository.findAllById(ids)).willReturn(List.of(interest1, interest2));

        // when
        List<InterestResponse> responses = interestService.getInterestsByIds(ids);

        // then
        assertThat(responses).hasSize(2);
        verify(interestRepository, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("모든 Interest 목록을 정상적으로 조회한다")
    void getAllInterests_Success() {
        // given
        Interest interest1 = InterestFixture.defaultInterest1();
        Interest interest2 = InterestFixture.defaultInterest2();
        given(interestRepository.findAll()).willReturn(List.of(interest1, interest2));

        // when
        List<InterestResponse> responses = interestService.getAllInterests();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo(InterestFixture.DEFAULT_INTEREST_NAME_1);
        assertThat(responses.get(1).name()).isEqualTo(InterestFixture.DEFAULT_INTEREST_NAME_2);
        verify(interestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("이름 목록으로 Interest ID 목록을 정상적으로 조회한다 (단일 쿼리)")
    void getInterestIdsByNames_Success() {
        // given
        String name1 = InterestFixture.DEFAULT_INTEREST_NAME_1;
        String name2 = InterestFixture.DEFAULT_INTEREST_NAME_2;
        List<String> names = List.of(name1, name2);

        Interest interest1 = Interest.builder().name(name1).build();
        Interest interest2 = Interest.builder().name(name2).build();
        ReflectionTestUtils.setField(interest1, "id", 1L);
        ReflectionTestUtils.setField(interest2, "id", 2L);
        List<Interest> interests = List.of(interest1, interest2);

        given(interestRepository.findAllByNameIn(names)).willReturn(interests);

        // when
        List<Long> ids = interestService.getInterestIdsByNames(names);

        // then
        assertThat(ids).containsExactly(1L, 2L);
        verify(interestRepository, times(1)).findAllByNameIn(names);
    }

    @Test
    @DisplayName("이름 목록 조회 시 결과 개수가 다르면 예외를 발생시킨다")
    void getInterestIdsByNames_SizeMismatch() {
        // given
        String name1 = InterestFixture.DEFAULT_INTEREST_NAME_1;
        String nonExistingName = "존재하지 않는 이름";
        List<String> names = List.of(name1, nonExistingName);

        Interest interest1 = Interest.builder().name(name1).build();
        ReflectionTestUtils.setField(interest1, "id", 1L);
        List<Interest> interests = List.of(interest1); // DB는 1개만 반환

        given(interestRepository.findAllByNameIn(names)).willReturn(interests);

        // when & then
        assertThatThrownBy(() -> interestService.getInterestIdsByNames(names))
                .isInstanceOf(BusinessException.class);
        verify(interestRepository, times(1)).findAllByNameIn(names);
    }
}
