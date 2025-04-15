package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentSearchGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchEstablishmentsUseCase {

    private final EstablishmentSearchGateway searchGateway;

    public SearchEstablishmentsUseCase(EstablishmentSearchGateway searchGateway) {
        this.searchGateway = searchGateway;
    }

    public List<Establishment> execute(EstablishmentSearchFilter filter) {
        return searchGateway.search(filter);
    }
}
