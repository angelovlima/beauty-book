package br.com.beauty_book.professional_management.config.exception;

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
