package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.booking.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfessionalIntegrationGatewayTest {

    private ProfessionalClient client;
    private ProfessionalIntegrationGateway gateway;

    @BeforeEach
    void setup() {
        client = mock(ProfessionalClient.class);
        gateway = new ProfessionalIntegrationGateway(client);
    }

    @Test
    void shouldReturnProfessionalWhenFound() {
        ProfessionalDto expected = new ProfessionalDto(
                1L,
                "Carla",
                "123.456.789-00",
                "11999999999",
                "carla@email.com"
        );
        when(client.findById(1L)).thenReturn(expected);

        Optional<ProfessionalDto> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionWhenProfessionalNotFound() {
        when(client.findById(1L)).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> gateway.findById(1L))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessage("Profissional com ID 1 não encontrado.");
    }

    @Test
    void shouldReturnAvailabilityList() {
        List<AvailabilityDto> expected = List.of(
                new AvailabilityDto(1, null, null),
                new AvailabilityDto(2, null, null)
        );
        when(client.getAvailabilityByProfessionalId(1L)).thenReturn(expected);

        List<AvailabilityDto> result = gateway.getAvailabilityByProfessionalId(1L);

        assertThat(result).isEqualTo(expected);
    }
}
