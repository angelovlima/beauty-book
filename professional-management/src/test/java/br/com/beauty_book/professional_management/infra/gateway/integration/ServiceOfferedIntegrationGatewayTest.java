package br.com.beauty_book.professional_management.infra.gateway.integration;

import br.com.beauty_book.professional_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.professional_management.domain.model.ServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.integration.client.ServiceOfferedClient;
import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import br.com.beauty_book.professional_management.infra.gateway.integration.mapper.ServiceOfferedDtoMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ServiceOfferedIntegrationGatewayTest {

    private ServiceOfferedClient client;
    private ServiceOfferedIntegrationGateway gateway;

    @BeforeEach
    void setUp() {
        client = mock(ServiceOfferedClient.class);
        ServiceOfferedDtoMapper mapper = new ServiceOfferedDtoMapper();
        gateway = new ServiceOfferedIntegrationGateway(client, mapper);
    }

    @Test
    @DisplayName("should return service when found")
    void shouldReturnServiceWhenFound() {
        ServiceOfferedDto dto = new ServiceOfferedDto(
                10L,
                "Corte Masculino",
                "Corte rápido",
                BigDecimal.valueOf(35),
                30
        );
        when(client.findById(10L)).thenReturn(dto);

        Optional<ServiceOffered> result = gateway.findById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(10L);
    }

    @Test
    @DisplayName("should throw exception when service not found")
    void shouldThrowExceptionWhenNotFound() {
        FeignException.NotFound notFound = new FeignException.NotFound(
                "Not Found",
                Request.create(Request.HttpMethod.GET, "/services/99", emptyMap(), null, StandardCharsets.UTF_8, null),
                null,
                null
        );

        when(client.findById(99L)).thenThrow(notFound);

        ServiceOfferedNotFoundException exception = assertThrows(ServiceOfferedNotFoundException.class, () -> gateway.findById(99L));
        assertThat(exception.getMessage()).isEqualTo("Serviço com ID 99 não encontrado.");
    }
}
