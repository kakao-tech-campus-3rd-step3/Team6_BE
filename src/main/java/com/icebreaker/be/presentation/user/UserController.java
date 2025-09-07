package com.icebreaker.be.presentation.user;

import com.icebreaker.be.application.user.UserService;
import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserIdWithTokenResponse;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import com.icebreaker.be.global.common.util.UriUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public final class UserController implements UserApiDocs {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<SuccessApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(users, "전체 유저 조회 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessApiResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(user, "단일 유저 조회 성공"));
    }

    @PostMapping()
    public ResponseEntity<SuccessApiResponse<UserIdWithTokenResponse>> createUser(
            @Valid @RequestBody CreateUserCommand cmd
    ) {
        UserIdWithTokenResponse response = userService.createUserIfNotExistsAndGenerateToken(cmd);

        return ResponseEntity
                .created(UriUtils.buildLocationUri(response.userId()))
                .body(ApiResponseFactory.success(response, "유저 생성 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success("유저 삭제 성공"));
    }
}
