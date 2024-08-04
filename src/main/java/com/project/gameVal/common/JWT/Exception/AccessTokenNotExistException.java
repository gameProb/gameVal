package com.project.gameVal.common.JWT.Exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class AccessTokenNotExistException extends RuntimeExceptionWithHttpStatus {
    public AccessTokenNotExistException() {
        super("accessToken is not exist", HttpStatus.NOT_FOUND);
    }
}
