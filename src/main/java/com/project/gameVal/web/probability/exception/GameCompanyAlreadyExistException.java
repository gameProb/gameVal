package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameCompanyAlreadyExistException extends RuntimeExceptionWithHttpStatus {
    private static final String DEFAULT_MESSAGE = "Game Company Already Exists";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.CONFLICT;

    public GameCompanyAlreadyExistException() {
        super(DEFAULT_MESSAGE, DEFAULT_STATUS);
    }
}
