package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.establishment_management.domain.model.Professional;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.ProfessionalClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.ProfessionalDto;
import br.com.beauty_book.establishment_management.infra.gateway.integration.mapper.ProfessionalDtoMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfessionalIntegrationGatewayTest {

    private ProfessionalClient client;
    private ProfessionalDtoMapper mapper;
    private ProfessionalIntegrationGateway gateway;

    @BeforeEach
    void setUp() {
        client = mock(ProfessionalClient.class);
        mapper = new ProfessionalDtoMapper();
        gateway = new ProfessionalIntegrationGateway(client, mapper);
    }

    @Test
    @DisplayName("should return professional when found")
    void shouldReturnProfessionalWhenFound() {
        ProfessionalDto dto = new ProfessionalDto(10L, "Mariana", "123.456.789-00", "11999999999", "mariana@email.com");
        when(client.findById(10L)).thenReturn(dto);

        Optional<Professional> result = gateway.findById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(10L);
        assertThat(result.get().name()).isEqualTo("Mariana");
    }

    @Test
    @DisplayName("should throw ProfessionalNotFoundException when professional is not found")
    void shouldThrowProfessionalNotFoundException() {
        FeignException.NotFound notFound = new FeignException.NotFound(
                "Not Found",
                Request.create(Request.HttpMethod.GET, "/professionals/10", emptyMap(), null, StandardCharsets.UTF_8, null),
                null,
                null
        );
        when(client.findById(10L)).thenThrow(notFound);

        ProfessionalNotFoundException ex = assertThrows(ProfessionalNotFoundException.class, () -> gateway.findById(10L));
        assertThat(ex.getMessage()).isEqualTo("Profissional com ID 10 não encontrado.");
    }
}
