package com.eralarm.eralarmbackend.base.rest_api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;
    private final List<ErrorDetail> details;
    private final OffsetDateTime timestamp;

    // 정적 팩토리 메서드
    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(true, null, "요청 성공", null,null,null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok( new ApiResponse<>(true, null, "요청 성공", data,null,null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data,String message) {
        return  ResponseEntity.ok(new ApiResponse<>(true, null, message, data,null,null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(String code, String message) {
        return ResponseEntity.ok(new ApiResponse<>(false,code,message,null,null,null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatusCode status, String code, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, code, message, null,null,OffsetDateTime.now()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatusCode status, String code, String message, List<ErrorDetail> details) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, code, message, null, details,OffsetDateTime.now()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> serverError(String code, String message) {
        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(false, code, message, null,null,OffsetDateTime.now()));
    }
}
