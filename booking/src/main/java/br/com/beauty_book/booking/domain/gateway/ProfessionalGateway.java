package br.com.beauty_book.booking.domain.gateway;

import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;

import java.util.List;
import java.util.Optional;

public interface ProfessionalGateway {
    Optional<ProfessionalDto> findById(Long id);
    List<AvailabilityDto> getAvailabilityByProfessionalId(Long id);
}
