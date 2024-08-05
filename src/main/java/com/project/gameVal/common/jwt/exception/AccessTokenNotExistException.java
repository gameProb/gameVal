package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class AccessTokenNotExistException extends RuntimeExceptionWithHttpStatus {
    public AccessTokenNotExistException() {
        super("accessToken is not exist", HttpStatus.NOT_FOUND);
    }
}
