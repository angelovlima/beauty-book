package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.CustomerClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.CustomerDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerIntegrationGateway implements CustomerGateway {

    private final CustomerClient client;

    @Override
    public Optional<CustomerDto> findById(Long id) {
        try {
            return Optional.ofNullable(client.findById(id));
        } catch (FeignException.NotFound e) {
            throw new CustomerNotFoundException("Cliente com ID " + id + " não encontrado.");
        }
    }
}
