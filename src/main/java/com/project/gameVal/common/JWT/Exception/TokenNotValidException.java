package com.project.gameVal.common.JWT.Exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class TokenNotValidException extends RuntimeExceptionWithHttpStatus {
    public TokenNotValidException() {
        super("token is not valid state", HttpStatus.UNAUTHORIZED);
    }
}
