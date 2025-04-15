package br.com.beauty_book.booking.domain.exception;

public class NotificationProfessionalNotFoundException extends RuntimeException {
    public NotificationProfessionalNotFoundException(Long id) {
        super("Profissional com ID %d não encontrado para envio de notificação.".formatted(id));
    }
}
