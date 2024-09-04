package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class ProbabilityTableException extends RuntimeExceptionWithHttpStatus {
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.NOT_FOUND;

    public ProbabilityTableException(String message) {
        super(message, DEFAULT_STATUS);
    }
}
