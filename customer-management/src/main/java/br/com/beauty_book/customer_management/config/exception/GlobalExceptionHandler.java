package br.com.beauty_book.customer_management.config.exception;

import br.com.beauty_book.customer_management.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerAlreadyExists(CustomerAlreadyExistsException ex) {
        var response = ApiErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCpf(InvalidCpfException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidEmail(InvalidEmailException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
