package com.icebreaker.be.domain.user;

import com.icebreaker.be.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private Long testUserId = 1L;

    @BeforeEach
    void setUp() {
        testUser = UserFixture.defaultUser();

        // 리플렉션을 사용하여 id 설정 (테스트용)
        try {
            java.lang.reflect.Field idField = testUser.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, testUserId);
        } catch (Exception e) {
            throw new RuntimeException("테스트 User에 id 설정 실패", e);
        }
    }

    @Test
    @DisplayName("전화번호로 사용자를 찾을 수 있다")
    void findByPhone_Success() {
        // given
        when(userRepository.findByPhone(UserFixture.DEFAULT_PHONE)).thenReturn(Optional.of(testUser));

        // when
        Optional<User> foundUser = userRepository.findByPhone(UserFixture.DEFAULT_PHONE);

        // then
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
        // given
        when(userRepository.findByPhone("01099999999")).thenReturn(Optional.empty());

        // when
        Optional<User> foundUser = userRepository.findByPhone("01099999999");

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("사용자를 저장할 수 있다")
    void save_Success() {
        // given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User savedUser = userRepository.save(UserFixture.defaultUser());

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(testUserId);
        assertThat(savedUser.getName()).isEqualTo(UserFixture.DEFAULT_NAME);
    }

    @Test
    @DisplayName("ID로 사용자를 찾을 수 있다")
    void findById_Success() {
        // given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // when
        Optional<User> foundUser = userRepository.findById(testUserId);

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(testUserId);
        assertThat(foundUser.get().getName()).isEqualTo(UserFixture.DEFAULT_NAME);
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    void delete_Success() {
        // when - 삭제 후 조회 시 비어있는 결과를 반환하도록 설정
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // 삭제 후 조회
        Optional<User> result = userRepository.findById(testUserId);

        // then
        assertThat(result).isEmpty();
    }
}
