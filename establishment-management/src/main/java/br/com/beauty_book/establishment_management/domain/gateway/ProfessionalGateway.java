package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.domain.model.Professional;

import java.util.Optional;

public interface ProfessionalGateway {

    Optional<Professional> findById(Long id);
}
