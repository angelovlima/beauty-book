package br.com.beauty_book.customer_management.domain.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer with ID %d not found".formatted(id));
    }
}
