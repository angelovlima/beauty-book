package br.com.beauty_book.customer_management.domain.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("Invalid email format: '%s'".formatted(email));
    }
}
