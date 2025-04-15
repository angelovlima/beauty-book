package br.com.beauty_book.professional_management.domain.gateway;

import br.com.beauty_book.professional_management.domain.model.ServiceOffered;

import java.util.Optional;

public interface ServiceOfferedGateway {
    Optional<ServiceOffered> findById(Long id);
}
