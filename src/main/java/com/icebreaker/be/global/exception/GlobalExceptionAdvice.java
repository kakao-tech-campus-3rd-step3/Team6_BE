package com.icebreaker.be.global.exception;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.ErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorApiResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return buildErrorResponse(errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(errorCode);
    }

    private ResponseEntity<ErrorApiResponse> buildErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponseFactory.error(errorCode));
    }
}
