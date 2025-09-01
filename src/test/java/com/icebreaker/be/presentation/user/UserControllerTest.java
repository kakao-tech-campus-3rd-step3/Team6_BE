package com.icebreaker.be.presentation.user;

import com.icebreaker.be.application.user.UserService;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.fixture.UserResponseFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("UserController 테스트")
    void userControllerTest() {
        // given
        List<UserResponse> users = UserResponseFixture.sampleUserResponseList();
        when(userService.getAllUsers()).thenReturn(users);

        // when & then - 예외가 발생하지 않는지만 확인
        userController.getAllUsers();
    }
}
