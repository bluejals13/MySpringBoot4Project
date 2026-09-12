package com.rookies6.myspringboot4project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus; 

@Getter
public class BusinessException extends RuntimeException {

    private final String message;
    private HttpStatus httpStatus;

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(String message, HttpStatus httpStatus, Object... args) {
        super(String.format(message, args));
        this.message = String.format(message, args);
        this.httpStatus = httpStatus;
    }
}