package com.icebreaker.be.presentation.user;

import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "사용자 관리 API")
public sealed interface UserApiDocs permits UserController {

    @Operation(
            summary = "전체 사용자 조회",
            description = "등록된 모든 사용자 정보를 조회합니다."
    )
    ResponseEntity<SuccessApiResponse<List<UserResponse>>> getAllUsers();

    @Operation(
            summary = "단일 사용자 조회",
            description = "ID를 통해 특정 사용자 정보를 조회합니다."
    )
    ResponseEntity<SuccessApiResponse<UserResponse>> getUserById(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "사용자 생성",
            description = "새로운 사용자를 생성하며, 이미 존재하면 스킵합니다."
    )
    ResponseEntity<SuccessApiResponse<Void>> createUser(
            @Parameter(description = "사용자 생성 정보", required = true)
            @RequestBody CreateUserCommand cmd
    );

    @Operation(
            summary = "사용자 삭제",
            description = "ID를 통해 특정 사용자를 삭제합니다."
    )
    ResponseEntity<SuccessApiResponse<Void>> deleteUser(
            @Parameter(description = "삭제할 사용자 ID", required = true, example = "1")
            @PathVariable Long id
    );
}
