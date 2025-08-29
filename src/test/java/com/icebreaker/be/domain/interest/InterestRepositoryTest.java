package com.icebreaker.be.domain.interest;

import com.icebreaker.be.fixture.InterestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("InterestRepository 테스트")
class InterestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InterestRepository interestRepository;

    @BeforeEach
    void setUp() {
        entityManager.persist(InterestFixture.defaultInterest1());
        entityManager.persist(InterestFixture.defaultInterest2());
        entityManager.persist(InterestFixture.defaultInterest3());
        entityManager.flush();
    }

    @Test
    @DisplayName("이름 목록으로 Interest 목록을 정확히 조회한다")
    void findAllByNameIn_Success() {
        // given
        List<String> namesToFind = List.of(InterestFixture.DEFAULT_INTEREST_NAME_1, InterestFixture.DEFAULT_INTEREST_NAME_3);

        // when
        List<Interest> foundInterests = interestRepository.findAllByNameIn(namesToFind);

        // then
        assertThat(foundInterests).hasSize(2);
        assertThat(foundInterests).extracting(Interest::getName)
                .containsExactlyInAnyOrder(InterestFixture.DEFAULT_INTEREST_NAME_1, InterestFixture.DEFAULT_INTEREST_NAME_3);
    }

    @Test
    @DisplayName("이름 목록에 존재하지 않는 이름이 포함되어 있어도, 존재하는 것들만 조회한다")
    void findAllByNameIn_WithNonExistingNames() {
        // given
        List<String> namesToFind = List.of(InterestFixture.DEFAULT_INTEREST_NAME_1, "코딩", "산책");

        // when
        List<Interest> foundInterests = interestRepository.findAllByNameIn(namesToFind);

        // then
        assertThat(foundInterests).hasSize(1);
        assertThat(foundInterests.get(0).getName()).isEqualTo(InterestFixture.DEFAULT_INTEREST_NAME_1);
    }
}
