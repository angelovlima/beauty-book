package br.com.beauty_book.professional_management.domain.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Invalid email format: '%s'".formatted(email));
    }
}
