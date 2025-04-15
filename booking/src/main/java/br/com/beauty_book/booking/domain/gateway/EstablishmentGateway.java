package br.com.beauty_book.booking.domain.gateway;

import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;

import java.util.List;

public interface EstablishmentGateway {

    EstablishmentDto findById(Long id);

    ServiceOfferedDto getServiceById(Long serviceId);

    Integer getServiceDuration(Long serviceId);

    List<OperatingHourDto> getOperatingHoursByEstablishmentId(Long establishmentId);
}
