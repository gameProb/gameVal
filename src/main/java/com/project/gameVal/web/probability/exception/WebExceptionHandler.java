package com.project.gameVal.web.probability.exception;

import com.project.gameVal.common.exception.RuntimeExceptionWithHttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(GameAlreadyExistException.class)
    public ResponseEntity<String> handleGameAlreadyExistException(GameAlreadyExistException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(GameCompanyAlreadyExistException.class)
    public ResponseEntity<String> handleGameCompanyAlreadyExistException(GameCompanyAlreadyExistException ex) {
        return buildResponseEntity(ex);
    }

    @ExceptionHandler(GameCompanyNotFoundException.class)
    public ResponseEntity<String> handleGameCompanyNotFoundException(GameCompanyNotFoundException ex) {
        return buildResponseEntity(ex);
    }

    private ResponseEntity<String> buildResponseEntity(RuntimeExceptionWithHttpStatus ex) {
        // 예외 클래스에서 HTTP 상태 코드를 가져오는 메서드 호출
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }
}
