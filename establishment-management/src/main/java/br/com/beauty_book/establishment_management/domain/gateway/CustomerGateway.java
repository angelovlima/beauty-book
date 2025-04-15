package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.CustomerDto;

import java.util.Optional;

public interface CustomerGateway {
    Optional<CustomerDto> findById(Long id);
}
