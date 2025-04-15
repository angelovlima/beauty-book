package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.booking.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.booking.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.booking.infra.gateway.integration.client.EstablishmentClient;
import br.com.beauty_book.booking.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
import br.com.beauty_book.booking.infra.gateway.integration.mapper.ServiceOfferedDtoMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EstablishmentIntegrationGateway implements EstablishmentGateway {

    private final EstablishmentClient client;
    private final ServiceOfferedDtoMapper mapper;
    private final ServiceOfferedClient serviceClient;

    @Override
    public EstablishmentDto findById(Long id) {
        try {
            return client.findById(id);
        } catch (FeignException.NotFound e) {
            throw new EstablishmentNotFoundException("Estabelecimento com ID " + id + " não encontrado.");
        }
    }

    @Override
    public ServiceOfferedDto getServiceById(Long serviceId) {
        try {
            return serviceClient.findById(serviceId);
        } catch (FeignException.NotFound e) {
            throw new ServiceOfferedNotFoundException("Serviço com ID " + serviceId + " não encontrado.");
        }
    }

    @Override
    public Integer getServiceDuration(Long serviceId) {
        return mapper.extractDurationMinutes(getServiceById(serviceId));
    }

    @Override
    public List<OperatingHourDto> getOperatingHoursByEstablishmentId(Long establishmentId) {
        return client.getOperatingHoursByEstablishmentId(establishmentId);
    }

}
