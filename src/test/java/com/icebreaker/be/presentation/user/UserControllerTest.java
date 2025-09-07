package com.icebreaker.be.presentation.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.application.user.UserService;
import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserIdWithTokenResponse;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.fixture.CreateUserCommandFixture;
import com.icebreaker.be.fixture.UserResponseFixture;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import com.icebreaker.be.global.exception.MvcGlobalExceptionAdvice;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .setControllerAdvice(new MvcGlobalExceptionAdvice())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("모든 사용자를 조회할 수 있다")
    void getAllUsers_Success() throws Exception {
        // given
        List<UserResponse> users = UserResponseFixture.sampleUserResponseList();
        when(userService.getAllUsers()).thenReturn(users);

        // when & then
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("전체 유저 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("홍길동"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("김철수"));
    }

    @Test
    @DisplayName("빈 사용자 목록을 조회할 수 있다")
    void getAllUsers_EmptyList() throws Exception {
        // given
        when(userService.getAllUsers()).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다")
    void getUserById_Success() throws Exception {
        // given
        Long userId = 1L;
        UserResponse user = UserResponseFixture.defaultUserResponse();
        when(userService.getUserById(userId)).thenReturn(user);

        // when & then
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("단일 유저 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.age").value(25))
                .andExpect(jsonPath("$.data.mbti").value("INTJ"));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자를 조회하면 404 에러가 발생한다")
    void getUserById_NotFound() throws Exception {
        // given
        Long userId = 999L;
        when(userService.getUserById(userId)).thenThrow(
                new BusinessException(ErrorCode.USER_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("유효한 데이터로 사용자를 생성할 수 있다")
    void createUser_Success() throws Exception {
        // given
        CreateUserCommand command = CreateUserCommandFixture.validCommand();
        UserIdWithTokenResponse response = new UserIdWithTokenResponse(1L, "test.jwt.token");
        when(userService.createUserIfNotExistsAndGenerateToken(
                any(CreateUserCommand.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("유저 생성 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.token").value("test.jwt.token"));
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    void deleteUser_Success() throws Exception {
        // given
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // when & then
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("유저 삭제 성공"));
    }
}
