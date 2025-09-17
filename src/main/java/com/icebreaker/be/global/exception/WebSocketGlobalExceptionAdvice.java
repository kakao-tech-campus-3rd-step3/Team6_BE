package com.icebreaker.be.global.exception;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.ErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class WebSocketGlobalExceptionAdvice {

    @MessageExceptionHandler(BusinessException.class)
    @SendToUser("/queue/errors")
    public ErrorApiResponse<Void> handleBusinessException(BusinessException ex) {
        log.error("BusinessException occurred: {}", ex.getMessage(), ex);
        return ApiResponseFactory.error(ex.getErrorCode());
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ErrorApiResponse<Void> handleAllException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return ApiResponseFactory.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
