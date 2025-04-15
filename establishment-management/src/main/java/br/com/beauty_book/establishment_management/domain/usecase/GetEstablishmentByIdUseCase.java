package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.springframework.stereotype.Service;

@Service
public class GetEstablishmentByIdUseCase {

    private final EstablishmentGateway gateway;

    public GetEstablishmentByIdUseCase(EstablishmentGateway gateway) {
        this.gateway = gateway;
    }

    public Establishment execute(Long id) {
        return gateway.findById(id)
                .orElseThrow(() -> new EstablishmentNotFoundException(id));
    }
}
