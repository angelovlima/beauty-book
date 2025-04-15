package br.com.beauty_book.establishment_management.domain.exception;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(Long customerId, Long establishmentId) {
        super("O cliente com ID %d já avaliou o estabelecimento com ID %d.".formatted(customerId, establishmentId));
    }
}
