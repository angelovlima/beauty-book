package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.booking.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.booking.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessionalIntegrationGateway implements ProfessionalGateway {

    private final ProfessionalClient client;

    @Override
    public Optional<ProfessionalDto> findById(Long id) {
        try {
            return Optional.ofNullable(client.findById(id));
        } catch (FeignException.NotFound e) {
            throw new ProfessionalNotFoundException("Profissional com ID " + id + " não encontrado.");
        }
    }

    @Override
    public List<AvailabilityDto> getAvailabilityByProfessionalId(Long id) {
        return client.getAvailabilityByProfessionalId(id);
    }
}
