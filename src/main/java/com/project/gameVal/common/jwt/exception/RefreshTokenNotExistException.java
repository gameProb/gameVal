package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotExistException extends RuntimeExceptionWithHttpStatus {
    public RefreshTokenNotExistException() {
        super("refreshToken is not exist", HttpStatus.NOT_FOUND);
    }
}
