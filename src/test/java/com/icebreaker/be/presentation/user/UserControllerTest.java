package com.icebreaker.be.presentation.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebreaker.be.application.user.UserService;
import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.fixture.CreateUserCommandFixture;
import com.icebreaker.be.fixture.UserResponseFixture;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("모든 사용자를 조회할 수 있다")
    void getAllUsers_Success() throws Exception {
        List<UserResponse> users = UserResponseFixture.sampleUserResponseList();
        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("전체 유저 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("홍길동"))
                .andExpect(jsonPath("$.data[0].phone").value("01012345678"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("김철수"));
    }

    @Test
    @DisplayName("빈 사용자 목록을 조회할 수 있다")
    void getAllUsers_EmptyList() throws Exception {
        given(userService.getAllUsers()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("ID로 사용자를 조회할 수 있다")
    void getUserById_Success() throws Exception {
        Long userId = 1L;
        UserResponse user = UserResponseFixture.defaultUserResponse();
        given(userService.getUserById(userId)).willReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("단일 유저 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.phone").value("01012345678"))
                .andExpect(jsonPath("$.data.age").value(25))
                .andExpect(jsonPath("$.data.mbti").value("INTJ"))
                .andExpect(jsonPath("$.data.introduction").value("안녕하세요"));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자를 조회하면 404 에러가 발생한다")
    void getUserById_NotFound() throws Exception {
        Long userId = 999L;
        given(userService.getUserById(userId)).willThrow(
                new BusinessException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("유효한 데이터로 사용자를 생성할 수 있다")
    void createUser_Success() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.validCommand();
        Long userId = 1L;
        given(userService.createUserIfNotExists(any(CreateUserCommand.class))).willReturn(userId);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("유저 생성 성공"))
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1"));
    }

    @Test
    @DisplayName("이름이 빈 값이면 400 에러가 발생한다")
    void createUser_InvalidName() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.invalidNameCommand();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 전화번호 형식이면 400 에러가 발생한다")
    void createUser_InvalidPhone() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.invalidPhoneCommand();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("나이가 범위를 벗어나면 400 에러가 발생한다")
    void createUser_InvalidAge() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.invalidAgeCommand();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이름이 20자를 초과하면 400 에러가 발생한다")
    void createUser_NameTooLong() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.tooLongNameCommand();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("소개가 500자를 초과하면 400 에러가 발생한다")
    void createUser_IntroductionTooLong() throws Exception {
        CreateUserCommand command = CreateUserCommandFixture.tooLongIntroductionCommand();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자를 삭제할 수 있다")
    void deleteUser_Success() throws Exception {
        Long userId = 1L;
        willDoNothing().given(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("유저 삭제 성공"));
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제하려고 하면 404 에러가 발생한다")
    void deleteUser_NotFound() throws Exception {
        Long userId = 999L;
        willThrow(new BusinessException(ErrorCode.USER_NOT_FOUND))
                .given(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}
