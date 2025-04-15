package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.domain.model.Establishment;

import java.util.List;
import java.util.Optional;

public interface EstablishmentGateway {

    Establishment save(Establishment establishment);

    Optional<Establishment> findById(Long id);

    List<Establishment> findAll();

    void deleteById(Long id);

    Establishment update(Long id, Establishment updated);
}
