package com.newfangled.flockbackend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionDto> undefinedException(Exception e) {
        e.printStackTrace();
        final ExceptionDto exceptionDto = new ExceptionDto("서버에 오류가 발생하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionDto> validException(
            MethodArgumentNotValidException validException) {
        final ExceptionDto exceptionDto = new ExceptionDto("유효성 검사 실패: "
                + validException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exceptionDto);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionDto> businessException(BusinessException e) {
        final ExceptionDto exceptionDto = new ExceptionDto(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(exceptionDto);
    }
}
