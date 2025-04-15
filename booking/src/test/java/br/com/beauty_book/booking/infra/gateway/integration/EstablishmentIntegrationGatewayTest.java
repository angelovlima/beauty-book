package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.booking.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.booking.infra.gateway.integration.client.EstablishmentClient;
import br.com.beauty_book.booking.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
import br.com.beauty_book.booking.infra.gateway.integration.mapper.ServiceOfferedDtoMapper;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstablishmentIntegrationGatewayTest {

    private EstablishmentClient establishmentClient;
    private ServiceOfferedClient serviceOfferedClient;
    private ServiceOfferedDtoMapper mapper;
    private EstablishmentIntegrationGateway gateway;

    @BeforeEach
    void setup() {
        establishmentClient = mock(EstablishmentClient.class);
        serviceOfferedClient = mock(ServiceOfferedClient.class);
        mapper = mock(ServiceOfferedDtoMapper.class);
        gateway = new EstablishmentIntegrationGateway(establishmentClient, mapper, serviceOfferedClient);
    }

    @Test
    void shouldReturnEstablishmentWhenFound() {
        EstablishmentDto expected = new EstablishmentDto(1L, "Salão Top", "Rua A", "foto.jpg");
        when(establishmentClient.findById(1L)).thenReturn(expected);

        EstablishmentDto result = gateway.findById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhenEstablishmentNotFound() {
        when(establishmentClient.findById(1L)).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> gateway.findById(1L))
                .isInstanceOf(EstablishmentNotFoundException.class)
                .hasMessage("Estabelecimento com ID 1 não encontrado.");
    }

    @Test
    void shouldReturnServiceWhenFound() {
        ServiceOfferedDto expected = new ServiceOfferedDto(1L, 1L, "Corte", "Desc", new BigDecimal("10.0"), 30);
        when(serviceOfferedClient.findById(1L)).thenReturn(expected);

        ServiceOfferedDto result = gateway.getServiceById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhenServiceNotFound() {
        when(serviceOfferedClient.findById(1L)).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> gateway.getServiceById(1L))
                .isInstanceOf(ServiceOfferedNotFoundException.class)
                .hasMessage("Serviço com ID 1 não encontrado.");
    }

    @Test
    void shouldReturnServiceDuration() {
        ServiceOfferedDto service = new ServiceOfferedDto(1L, 1L, "Corte", "Desc", new BigDecimal("10.0"), 30);
        when(serviceOfferedClient.findById(1L)).thenReturn(service);
        when(mapper.extractDurationMinutes(service)).thenReturn(30);

        Integer duration = gateway.getServiceDuration(1L);

        assertThat(duration).isEqualTo(30);
    }

    @Test
    void shouldReturnOperatingHours() {
        List<OperatingHourDto> expected = List.of(
                new OperatingHourDto(1, null, null)
        );
        when(establishmentClient.getOperatingHoursByEstablishmentId(1L)).thenReturn(expected);

        List<OperatingHourDto> result = gateway.getOperatingHoursByEstablishmentId(1L);

        assertThat(result).isEqualTo(expected);
    }
}
