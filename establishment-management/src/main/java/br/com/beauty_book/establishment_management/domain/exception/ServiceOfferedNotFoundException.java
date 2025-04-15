package br.com.beauty_book.establishment_management.domain.exception;

public class ServiceOfferedNotFoundException extends RuntimeException {

    public ServiceOfferedNotFoundException(Long id) {
        super("Serviço com ID " + id + " não encontrado.");
    }
}
