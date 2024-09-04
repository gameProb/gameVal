package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameAlreadyExistException extends RuntimeExceptionWithHttpStatus {
    private static final String DEFAULT_MESSAGE = "Game Already Exists";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.CONFLICT;

    public GameAlreadyExistException() {
        super(DEFAULT_MESSAGE, DEFAULT_STATUS);
    }
}
