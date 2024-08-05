package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends RuntimeExceptionWithHttpStatus {
    public RefreshTokenExpiredException() {
        super("refreshToken is expired", HttpStatus.UNAUTHORIZED);
    }
}
