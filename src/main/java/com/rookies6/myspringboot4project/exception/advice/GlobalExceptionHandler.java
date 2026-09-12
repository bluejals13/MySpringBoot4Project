package com.rookies6.myspringboot4project.exception.advice;

import com.rookies6.myspringboot4project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // @Valid 검증 실패
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        log.warn("Validation Error: {}", errors);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "입력값이 올바르지 않습니다.");
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // =========================
    // 잘못된 인자
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {

        log.warn("Illegal Argument: {}", ex.getMessage());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // =========================
    // BusinessException
    // =========================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            BusinessException ex
    ) {

        log.warn(
                "Business Exception [{}]: {}",
                ex.getErrorCode(),
                ex.getMessage()
        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put(
                "status",
                ex.getErrorCode()
                        .getHttpStatus()
                        .value()
        );

        response.put(
                "message",
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(response);
    }


    // =========================
    // 기타 서버 오류
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex
    ) {

        log.error(
                "Internal Server Error: ",
                ex
        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        response.put(
                "message",
                "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
