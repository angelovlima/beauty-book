package br.com.beauty_book.booking.domain.exception;

public class ProfessionalNotFoundException extends RuntimeException {
    public ProfessionalNotFoundException(String message) {
        super(message);
    }
}
