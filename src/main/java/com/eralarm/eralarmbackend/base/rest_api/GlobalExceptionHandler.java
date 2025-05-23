package com.eralarm.eralarmbackend.base.rest_api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public record GlobalExceptionHandler() {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
        return ApiResponse.serverError("INTERNAL_ERROR", "서버 오류가 발생했습니다.");
    }
}
