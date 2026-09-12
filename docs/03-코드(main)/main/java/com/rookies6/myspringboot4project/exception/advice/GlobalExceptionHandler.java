package com.rookies6.myspringboot4project.exception.advice;

import com.rookies6.myspringboot4project.exception.BusinessException;
import com.rookies6.myspringboot4project.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid 유효성 검사 실패 (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("잘못된 요청 파라미터입니다.");
        log.warn("Validation Error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.error(errorMessage));
    }

    // 2. 잘못된 인자 예외 (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal Argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.error(ex.getMessage()));
    }

    // 3. 직접 정의한 비즈니스 예외 처리 (BookService 등에서 발생)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse<Void>> handleBusinessException(BusinessException ex) {
        log.warn("Business Exception: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ErrorResponse.error(ex.getMessage()));
    }

    // 4. 그 외 모든 서버 내부 예외 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleAllExceptions(Exception ex) {
        log.error("Internal Server Error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.error("서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}