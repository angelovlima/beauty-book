package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.establishment_management.domain.model.Professional;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.mapper.ProfessionalDtoMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessionalIntegrationGateway implements ProfessionalGateway {

    private final ProfessionalClient client;
    private final ProfessionalDtoMapper mapper;

    @Override
    public Optional<Professional> findById(Long id) {
        try {
            var dto = client.findById(id);
            return Optional.ofNullable(mapper.toDomain(dto));
        } catch (FeignException.NotFound e) {
            throw new ProfessionalNotFoundException("Profissional com ID " + id + " não encontrado.");
        }
    }
}
