package com.zb.exception;

import static com.zb.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.zb.type.ErrorCode.INVALID_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // TODO : 나중엔 세분화 필요할듯?
    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleGlobalException(CustomException e) {
        log.error(e.getErrorCode() + " is occurred", e);

        return ErrorResponse.builder()
                            .errorCode(e.getErrorCode())
                            .errorMessage(e.getErrorMessage())
                            .build();
    }


    // Validation 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage() + " is occurred", e);

        return ErrorResponse.builder()
                            .errorCode(INVALID_REQUEST)
                            .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                            .build();
    }

    // DB 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage() + " is occurred", e);

        return ErrorResponse.builder()
                            .errorCode(INVALID_REQUEST)
                            .errorMessage(INVALID_REQUEST.getDescription())
                            .build();
    }

    // 기타 에러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("Exception is occurred", e);

        return ErrorResponse.builder()
                            .errorCode(INTERNAL_SERVER_ERROR)
                            .errorMessage(INTERNAL_SERVER_ERROR.getDescription())
                            .build();
    }
}
