package com.newfangled.flockbackend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionDto> undefinedException(Exception e) {
        final ExceptionDto exceptionDto = new ExceptionDto("서버에 오류가 발생하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionDto> businessException(BusinessException e) {
        final ExceptionDto exceptionDto = new ExceptionDto(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(exceptionDto);
    }
}
