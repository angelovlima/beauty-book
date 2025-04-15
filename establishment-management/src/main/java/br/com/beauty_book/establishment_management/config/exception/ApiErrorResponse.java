package br.com.beauty_book.establishment_management.config.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {
    public static ApiErrorResponse of(int status, String message) {
        return new ApiErrorResponse(status, message, LocalDateTime.now());
    }
}
