package com.icebreaker.be.infra.persistence.redis;

import com.icebreaker.be.global.exception.ErrorCode;

public interface StatusMapper<S> {

    ErrorCode toErrorCode(S status);

    boolean isSuccess(S status);

    Class<S> getStatusClass();
}
