package com.rookies6.myspringboot4project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    RESOURCE_NOT_FOUND(
            "Resource not found",
            HttpStatus.NOT_FOUND
    ),

    STUDENT_NUMBER_DUPLICATE(
            "Student number already exists: %s",
            HttpStatus.CONFLICT
    ),

    INVALID_INPUT(
            "Invalid input",
            HttpStatus.BAD_REQUEST
    ),

    INTERNAL_SERVER_ERROR(
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }

        return String.format(message, args);
    }
}
