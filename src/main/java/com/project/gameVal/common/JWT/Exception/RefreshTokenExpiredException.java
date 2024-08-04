package com.project.gameVal.common.JWT.Exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends RuntimeExceptionWithHttpStatus {
    public RefreshTokenExpiredException() {
        super("refreshToken is expired", HttpStatus.UNAUTHORIZED);
    }
}
