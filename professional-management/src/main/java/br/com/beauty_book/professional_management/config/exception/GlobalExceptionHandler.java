package br.com.beauty_book.professional_management.config.exception;

import br.com.beauty_book.professional_management.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProfessionalNotFound(ProfessionalNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProfessionalAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleProfessionalAlreadyExists(ProfessionalAlreadyExistsException ex) {
        var response = ApiErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ServiceOfferedNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceNotFound(ServiceOfferedNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
