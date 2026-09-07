package com.rookies6.myspringboot4project.exception.advice;

import lombok.Getter;

@Getter
public enum ValidationErrorResponse {

    RESOURCE_NOT_FOUND("해당 자원을 찾을 수 없습니다.", 404),
    STUDENT_NUMBER_DUPLICATE("이미 존재하는 학번입니다: %s", 409),
    INVALID_INPUT("입력값이 올바르지 않습니다.", 400),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", 500);

    private final String message;
    private final int status;

    ValidationErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        return String.format(message, args);
    }
}