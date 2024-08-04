package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.Exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.HttpStatus;

public class GameCompanyAlreadyExistException extends RuntimeExceptionWithHttpStatus {
    public GameCompanyAlreadyExistException() {
        super("Game Company Already Exist", HttpStatus.CONFLICT);
    }
}
