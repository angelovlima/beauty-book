package br.com.beauty_book.establishment_management.domain.exception;

public class ProfessionalNotFoundException extends RuntimeException {

    public ProfessionalNotFoundException(String message) {
        super(message);
    }
}
