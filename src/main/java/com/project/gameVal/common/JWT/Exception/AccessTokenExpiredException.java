package com.project.gameVal.common.JWT.Exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class AccessTokenExpiredException extends RuntimeExceptionWithHttpStatus {
    public AccessTokenExpiredException() {
        super("accessToken is expired", HttpStatus.UNAUTHORIZED);
    }
}
