package com.icebreaker.be.global.exception;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.ErrorApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return buildErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse<List<ValidationError>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<ValidationError> errors = extractValidationErrors(ex);
        return buildErrorResponse(ErrorCode.INVALID_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse<Void>> handleException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    
    private List<ValidationError> extractValidationErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();
    }

    private <T> ResponseEntity<ErrorApiResponse<T>> buildErrorResponse(ErrorCode errorCode,
            T data) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponseFactory.error(errorCode, data));
    }

    private <T> ResponseEntity<ErrorApiResponse<T>> buildErrorResponse(ErrorCode errorCode) {
        return buildErrorResponse(errorCode, null);
    }
}