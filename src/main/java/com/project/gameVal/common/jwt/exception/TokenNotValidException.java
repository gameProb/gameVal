package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class TokenNotValidException extends RuntimeExceptionWithHttpStatus {
    public TokenNotValidException() {
        super("token is not valid state", HttpStatus.UNAUTHORIZED);
    }
}
