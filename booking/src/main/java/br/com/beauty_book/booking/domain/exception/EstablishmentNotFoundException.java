package br.com.beauty_book.booking.domain.exception;

public class EstablishmentNotFoundException extends RuntimeException {
    public EstablishmentNotFoundException(String message) {
        super(message);
    }
}
