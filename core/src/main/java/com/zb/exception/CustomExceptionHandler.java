package com.zb.exception;

import static com.zb.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.zb.type.ErrorCode.INVALID_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(CustomException e) {
        log.error(e.getErrorCode() + " is occurred", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorCode(e.getErrorCode())
                                                   .errorMessage(e.getMessage())
                                                   .build();

        return ResponseEntity.status(e.getErrorCode().getStatus())
                             .body(errorResponse);
    }


    // Validation 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage() + " is occurred", e);

        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors()
         .forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST)
                             .body(errors);
    }

    // DB 에러
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage() + " is occurred", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorCode(INVALID_REQUEST)
                                                   .errorMessage(INVALID_REQUEST.getMessage())
                                                   .build();

        return ResponseEntity.status(errorResponse.getErrorCode().getStatus())
                             .body(errorResponse);
    }

    // 기타 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception is occurred", e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .errorCode(INTERNAL_SERVER_ERROR)
                                                   .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                                                   .build();

        return ResponseEntity.status(errorResponse.getErrorCode().getStatus())
                             .body(errorResponse);
    }
}
