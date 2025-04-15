package br.com.beauty_book.professional_management.domain.exception;

public class ProfessionalAlreadyExistsException extends RuntimeException {

    public ProfessionalAlreadyExistsException(String cpf) {
        super("A professional with CPF " + cpf + " already exists.");
    }
}
