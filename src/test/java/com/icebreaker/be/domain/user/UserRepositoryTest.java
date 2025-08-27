package com.icebreaker.be.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.icebreaker.be.fixture.UserFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("전화번호로 사용자를 찾을 수 있다")
    void findByPhone_Success() {
        User user = UserFixture.defaultUser();
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByPhone(UserFixture.DEFAULT_PHONE);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(UserFixture.DEFAULT_NAME);
        assertThat(foundUser.get().getPhone()).isEqualTo(UserFixture.DEFAULT_PHONE);
        assertThat(foundUser.get().getAge()).isEqualTo(25);
        assertThat(foundUser.get().getMbti()).isEqualTo(UserFixture.DEFAULT_MBTI);
        assertThat(foundUser.get().getIntroduction()).isEqualTo("안녕하세요");
    }

    @Test
    @DisplayName("존재하지 않는 전화번호로 조회하면 빈 Optional을 반환한다")
    void findByPhone_NotFound() {
        Optional<User> foundUser = userRepository.findByPhone("01099999999");

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("사용자를 저장할 수 있다")
    void save_Success() {
        User user = UserFixture.defaultUser();
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(UserFixture.DEFAULT_NAME);
        assertThat(savedUser.getPhone()).isEqualTo(UserFixture.DEFAULT_PHONE);
    }

    @Test
    @DisplayName("ID로 사용자를 찾을 수 있다")
    void findById_Success() {
        User user = UserFixture.defaultUser();
        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(UserFixture.DEFAULT_NAME);
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    void delete_Success() {
        User user = UserFixture.defaultUser();
        User savedUser = entityManager.persistAndFlush(user);
        Long userId = savedUser.getId();

        userRepository.delete(savedUser);
        entityManager.flush();

        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();
    }
}
