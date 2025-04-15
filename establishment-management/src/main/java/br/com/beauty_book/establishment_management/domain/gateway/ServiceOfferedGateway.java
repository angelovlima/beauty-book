package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;

import java.util.Optional;

public interface ServiceOfferedGateway {

    Optional<ServiceOffered> findById(Long id);
}
