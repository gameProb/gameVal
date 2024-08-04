package com.project.gameVal.common.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RuntimeExceptionWithHttpStatus extends RuntimeException {

    private final HttpStatus httpStatus;

    public RuntimeExceptionWithHttpStatus(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
