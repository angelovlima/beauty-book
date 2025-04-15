package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import org.springframework.stereotype.Service;

@Service
public class DeleteEstablishmentUseCase {

    private final EstablishmentGateway gateway;

    public DeleteEstablishmentUseCase(EstablishmentGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Long id) {
        gateway.findById(id)
                .orElseThrow(() -> new EstablishmentNotFoundException(id));

        gateway.deleteById(id);
    }
}
