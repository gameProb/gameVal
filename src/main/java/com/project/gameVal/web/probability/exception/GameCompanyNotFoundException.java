package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameCompanyNotFoundException extends RuntimeExceptionWithHttpStatus {
    public GameCompanyNotFoundException() {
        super("Game Company Not Found", HttpStatus.NOT_FOUND);
    }
}
