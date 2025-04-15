package br.com.beauty_book.booking.domain.exception;

public class BookingAlreadyCompletedException extends RuntimeException {
    public BookingAlreadyCompletedException(String message) {
        super(message);
    }
}
