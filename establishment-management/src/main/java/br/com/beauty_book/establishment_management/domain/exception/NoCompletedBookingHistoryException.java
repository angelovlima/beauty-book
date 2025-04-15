package br.com.beauty_book.establishment_management.domain.exception;

public class NoCompletedBookingHistoryException extends RuntimeException {
    public NoCompletedBookingHistoryException(Long customerId, Long establishmentId) {
        super("Cliente com ID %d não possui histórico de atendimento COMPLETED no estabelecimento com ID %d.".formatted(customerId, establishmentId));
    }
}
