package br.com.beauty_book.professional_management.domain.exception;

public class ServiceOfferedNotFoundException extends RuntimeException {
    public ServiceOfferedNotFoundException(String message) {
        super(message);
    }
}
