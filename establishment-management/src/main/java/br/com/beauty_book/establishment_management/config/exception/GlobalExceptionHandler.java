package br.com.beauty_book.establishment_management.config.exception;

import br.com.beauty_book.establishment_management.domain.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstablishmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEstablishmentNotFound(EstablishmentNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(DomainException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        var response = ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), extractDeepestMessage(ex));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProfessionalNotFound(ProfessionalNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ServiceOfferedNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceOfferedNotFound(ServiceOfferedNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidReviewStarsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidReviewStars(InvalidReviewStarsException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmptyReviewCommentException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyReviewComment(EmptyReviewCommentException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleReviewAlreadyExists(ReviewAlreadyExistsException ex) {
        var response = ApiErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(NoCompletedBookingHistoryException.class)
    public ResponseEntity<ApiErrorResponse> handleNoCompletedBookingHistory(NoCompletedBookingHistoryException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    private String extractDeepestMessage(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
