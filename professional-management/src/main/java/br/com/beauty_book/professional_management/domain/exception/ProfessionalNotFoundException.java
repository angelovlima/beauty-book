package br.com.beauty_book.professional_management.domain.exception;

public class ProfessionalNotFoundException extends RuntimeException {

    public ProfessionalNotFoundException(Long id) {
        super("Profissional não encontrado com ID: " + id);
    }

    public ProfessionalNotFoundException(String cpf) {
        super("Profissional não encontrado com CPF: " + cpf);
    }
}
