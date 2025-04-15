package br.com.beauty_book.establishment_management.domain.exception;

public class EstablishmentNotFoundException extends DomainException {
    public EstablishmentNotFoundException(Long id) {
        super("Establishment with ID " + id + " not found.");
    }
}
