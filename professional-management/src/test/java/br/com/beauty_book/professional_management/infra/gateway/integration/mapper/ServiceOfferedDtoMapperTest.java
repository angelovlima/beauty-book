package br.com.beauty_book.professional_management.infra.gateway.integration.mapper;

import br.com.beauty_book.professional_management.domain.model.ServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceOfferedDtoMapperTest {

    private final ServiceOfferedDtoMapper mapper = new ServiceOfferedDtoMapper();

    @Test
    @DisplayName("should map ServiceOfferedDto to domain model")
    void shouldMapToDomain() {
        ServiceOfferedDto dto = new ServiceOfferedDto(
                10L,
                "Corte Masculino",
                "Corte básico com máquina",
                new BigDecimal("39.90"),
                30
        );

        ServiceOffered domain = mapper.toDomain(dto);

        assertThat(domain).isNotNull();
        assertThat(domain.id()).isEqualTo(10L);
        assertThat(domain.name()).isEqualTo("Corte Masculino");
    }
}
