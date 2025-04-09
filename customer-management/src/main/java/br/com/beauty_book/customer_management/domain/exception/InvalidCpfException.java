package br.com.beauty_book.customer_management.domain.exception;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException(String cpf) {
        super("Invalid CPF: '%s'. It must match the pattern XXX.XXX.XXX-XX".formatted(cpf));
    }
}

