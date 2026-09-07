package com.rookies6.myspringboot4project.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Builder
public class ErrorResponse<T> {

    private String message;
    private String timestamp;

    public static <T> ErrorResponse<T> error(String message) {
        return ErrorResponse.<T>builder()
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA)))
                .build();
    }
}