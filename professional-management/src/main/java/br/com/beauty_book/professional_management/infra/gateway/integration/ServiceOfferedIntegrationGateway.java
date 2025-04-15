package br.com.beauty_book.professional_management.infra.gateway.integration;

import br.com.beauty_book.professional_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.ServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.ServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.professional_management.infra.gateway.integration.mapper.ServiceOfferedDtoMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceOfferedIntegrationGateway implements ServiceOfferedGateway {

    private final ServiceOfferedClient client;
    private final ServiceOfferedDtoMapper mapper;

    @Override
    public Optional<ServiceOffered> findById(Long serviceOfferedId) {
        try {
            var dto = client.findById(serviceOfferedId);
            return Optional.ofNullable(mapper.toDomain(dto));
        } catch (FeignException.NotFound e) {
            throw new ServiceOfferedNotFoundException("Serviço com ID " + serviceOfferedId + " não encontrado.");
        }
    }
}
