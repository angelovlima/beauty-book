package br.com.beauty_book.booking.domain.exception;

public class CompletedBookingCannotBeCancelledException extends RuntimeException {
    public CompletedBookingCannotBeCancelledException(String message) {
        super(message);
    }
}
