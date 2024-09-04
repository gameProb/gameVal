package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameCompanyNotFoundException extends RuntimeExceptionWithHttpStatus {
    private static final String DEFAULT_MESSAGE = "Game Company Not Found";
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.NOT_FOUND;

    public GameCompanyNotFoundException() {
        super(DEFAULT_MESSAGE, DEFAULT_STATUS);
    }
}
