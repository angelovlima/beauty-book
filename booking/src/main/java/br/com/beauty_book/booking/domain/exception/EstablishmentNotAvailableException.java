package br.com.beauty_book.booking.domain.exception;

public class EstablishmentNotAvailableException extends RuntimeException {
    public EstablishmentNotAvailableException(String message) {
        super(message);
    }
}
