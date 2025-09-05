package com.icebreaker.be.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.icebreaker.be.config.TestLlmConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestLlmConfig.class)
@DisplayName("UserRepository 통합 테스트")
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("전화번호로 사용자를 조회할 수 있다")
    void findByPhone_Success() {
        // given
        User user = User.builder()
                .name("테스트 사용자")
                .phone("010-1234-5678")
                .age(25)
                .mbti(MbtiType.ENFP)
                .introduction("안녕하세요!")
                .build();

        entityManager.persistAndFlush(user);

        // when
        Optional<User> foundUser = userRepository.findByPhone("010-1234-5678");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("테스트 사용자");
        assertThat(foundUser.get().getPhone()).isEqualTo("010-1234-5678");
        assertThat(foundUser.get().getAge()).isEqualTo(25);
        assertThat(foundUser.get().getMbti()).isEqualTo(MbtiType.ENFP);
    }

    @Test
    @DisplayName("존재하지 않는 전화번호로 조회하면 빈 Optional을 반환한다")
    void findByPhone_NotFound() {
        // when
        Optional<User> foundUser = userRepository.findByPhone("010-9999-9999");

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("사용자를 저장하고 조회할 수 있다")
    void saveAndFind_Success() {
        // given
        User user = User.builder()
                .name("저장 테스트")
                .phone("010-5678-1234")
                .age(30)
                .mbti(MbtiType.INTJ)
                .introduction("테스트 자기소개")
                .build();

        // when
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getName()).isEqualTo("저장 테스트");
    }

    @Test
    @DisplayName("전화번호는 유니크 제약조건이 적용된다")
    void phoneUnique_Constraint() {
        // given
        User user1 = User.builder()
                .name("사용자1")
                .phone("010-1111-1111")
                .age(25)
                .mbti(MbtiType.ENFP)
                .introduction("첫 번째 사용자")
                .build();

        User user2 = User.builder()
                .name("사용자2")
                .phone("010-1111-1111") // 같은 전화번호
                .age(30)
                .mbti(MbtiType.INTJ)
                .introduction("두 번째 사용자")
                .build();

        // when
        userRepository.save(user1);
        entityManager.flush();

        // then
        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> {
                    userRepository.save(user2);
                    entityManager.flush();
                }
        );
    }
}
