package br.com.beauty_book.customer_management.domain.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String cpf) {
        super("A customer with CPF %s already exists.".formatted(cpf));
    }
}
