package com.project.gameVal.common.jwt.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<String> handleTokenNotValid(TokenNotValidException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(RefreshTokenNotExistException.class)
    public ResponseEntity<String> handleRefreshTokenNotExist(RefreshTokenNotExistException ex) {
        return buildResponseEntity(ex);
    }

    private ResponseEntity<String> buildResponseEntity(RuntimeExceptionWithHttpStatus ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }
}
