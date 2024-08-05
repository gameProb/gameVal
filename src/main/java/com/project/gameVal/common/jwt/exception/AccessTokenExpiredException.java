package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class AccessTokenExpiredException extends RuntimeExceptionWithHttpStatus {
    public AccessTokenExpiredException() {
        super("accessToken is expired", HttpStatus.UNAUTHORIZED);
    }
}
