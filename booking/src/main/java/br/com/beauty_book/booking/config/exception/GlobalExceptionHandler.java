package br.com.beauty_book.booking.config.exception;

import br.com.beauty_book.booking.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProfessionalNotFound(ProfessionalNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EstablishmentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEstablishmentNotFound(EstablishmentNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ServiceOfferedNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceNotFound(ServiceOfferedNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        var response = ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EstablishmentNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleEstablishmentNotAvailable(EstablishmentNotAvailableException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ProfessionalNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleProfessionalNotAvailable(ProfessionalNotAvailableException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingConflict(BookingConflictException ex) {
        var response = ApiErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingNotFound(BookingNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BookingAlreadyCancelledException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingAlreadyCancelled(BookingAlreadyCancelledException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CompletedBookingCannotBeCancelledException.class)
    public ResponseEntity<ApiErrorResponse> handleCompletedBookingCannotBeCancelled(CompletedBookingCannotBeCancelledException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BookingAlreadyCompletedException.class)
    public ResponseEntity<ApiErrorResponse> handleBookingAlreadyCompleted(BookingAlreadyCompletedException ex) {
        var response = ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotificationCustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotificationCustomerNotFound(NotificationCustomerNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NotificationProfessionalNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotificationProfessionalNotFound(NotificationProfessionalNotFoundException ex) {
        var response = ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
