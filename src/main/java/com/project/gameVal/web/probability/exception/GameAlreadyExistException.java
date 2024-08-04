package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameAlreadyExistException extends RuntimeExceptionWithHttpStatus {
    public GameAlreadyExistException() {
        super("Game Already Exist", HttpStatus.CONFLICT);
    }
}
