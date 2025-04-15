package br.com.beauty_book.booking.domain.gateway;

import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;

import java.util.Optional;

public interface CustomerGateway {
    Optional<CustomerDto> findById(Long id);
}
