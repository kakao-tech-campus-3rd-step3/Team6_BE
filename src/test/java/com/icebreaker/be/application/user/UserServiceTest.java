package com.icebreaker.be.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.domain.user.User;
import com.icebreaker.be.domain.user.UserRepository;
import com.icebreaker.be.fixture.CreateUserCommandFixture;
import com.icebreaker.be.fixture.UserFixture;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다")
    void getUserById_Success() {
        Long userId = 3L;
        User user = UserFixture.userWithId(userId, UserFixture.DEFAULT_NAME,
                UserFixture.DEFAULT_PHONE);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserResponse result = userService.getUserById(userId);

        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.name()).isEqualTo(UserFixture.DEFAULT_NAME);
        assertThat(result.phone()).isEqualTo(UserFixture.DEFAULT_PHONE);
        assertThat(result.age()).isEqualTo(25);
        assertThat(result.mbti()).isEqualTo(UserFixture.DEFAULT_MBTI);
        assertThat(result.introduction()).isEqualTo("안녕하세요");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자를 조회하면 예외가 발생한다")
    void getUserById_NotFound() {
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("모든 사용자를 조회할 수 있다")
    void getAllUsers_Success() {
        User user1 = UserFixture.userWithId(1L, UserFixture.DEFAULT_NAME,
                UserFixture.DEFAULT_PHONE);
        User user2 = UserFixture.userWithId(2L, "김철수", "01087654321");
        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo(UserFixture.DEFAULT_NAME);
        assertThat(result.get(1).name()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("빈 사용자 목록을 조회할 수 있다")
    void getAllUsers_EmptyList() {
        given(userRepository.findAll()).willReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("새로운 사용자를 생성할 수 있다")
    void createUserIfNotExists_NewUser() {
        CreateUserCommand command = CreateUserCommandFixture.validCommand();
        User savedUser = UserFixture.userWithId(2L, UserFixture.DEFAULT_NAME,
                UserFixture.DEFAULT_PHONE);

        given(userRepository.findByPhone(command.phone())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        Long result = userService.createUserIfNotExists(command);

        assertThat(result).isEqualTo(6L);
        then(userRepository).should(times(1)).findByPhone(command.phone());
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 전화번호로 사용자 생성 시 기존 사용자 ID를 반환한다")
    void createUserIfNotExists_ExistingUser() {
        CreateUserCommand command = CreateUserCommandFixture.validCommand();
        User existingUser = UserFixture.userWithId(3L, UserFixture.DEFAULT_NAME,
                UserFixture.DEFAULT_PHONE);

        given(userRepository.findByPhone(command.phone())).willReturn(Optional.of(existingUser));

        Long result = userService.createUserIfNotExists(command);

        assertThat(result).isEqualTo(4L);
        then(userRepository).should(times(1)).findByPhone(command.phone());
        then(userRepository).should(never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    void deleteUser_Success() {
        Long userId = 1L;
        User user = UserFixture.userWithId(userId, UserFixture.DEFAULT_NAME,
                UserFixture.DEFAULT_PHONE);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.deleteUser(userId);

        then(userRepository).should(times(1)).findById(userId);
        then(userRepository).should(times(1)).delete(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제하려고 하면 예외가 발생한다")
    void deleteUser_NotFound() {
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());

        then(userRepository).should(times(1)).findById(userId);
        then(userRepository).should(never()).delete(any(User.class));
    }
}
