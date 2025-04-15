package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListEstablishmentsUseCase {

    private final EstablishmentGateway gateway;

    public ListEstablishmentsUseCase(EstablishmentGateway gateway) {
        this.gateway = gateway;
    }

    public List<Establishment> execute() {
        return gateway.findAll();
    }
}
