package com.project.gameVal.common.JWT.Exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotExistException extends RuntimeExceptionWithHttpStatus {
    public RefreshTokenNotExistException() {
        super("refreshToken is not exist", HttpStatus.NOT_FOUND);
    }
}
