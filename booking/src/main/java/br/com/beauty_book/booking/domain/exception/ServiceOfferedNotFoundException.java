package br.com.beauty_book.booking.domain.exception;

public class ServiceOfferedNotFoundException extends RuntimeException {
    public ServiceOfferedNotFoundException(String message) {
        super(message);
    }
}
