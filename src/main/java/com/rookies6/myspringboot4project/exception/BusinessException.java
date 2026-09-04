package com.rookies6.myspringboot4project.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }

    public org.springframework.http.HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
