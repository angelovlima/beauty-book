package br.com.beauty_book.booking.domain.exception;

public class NotificationCustomerNotFoundException extends RuntimeException {
    public NotificationCustomerNotFoundException(Long id) {
        super("Cliente com ID %d não encontrado para envio de notificação.".formatted(id));
    }
}
