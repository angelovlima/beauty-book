package br.com.beauty_book.booking.domain.exception;

public class ProfessionalNotAvailableException extends RuntimeException {
    public ProfessionalNotAvailableException(String message) {
        super(message);
    }
}
