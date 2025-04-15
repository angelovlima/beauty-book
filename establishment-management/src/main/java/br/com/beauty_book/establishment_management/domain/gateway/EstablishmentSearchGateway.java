package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.domain.model.Establishment;

import java.util.List;

public interface EstablishmentSearchGateway {
    List<Establishment> search(EstablishmentSearchFilter filter);
}