package br.com.beauty_book.establishment_management.infra.gateway.integration.mapper;

import br.com.beauty_book.establishment_management.domain.model.Professional;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.ProfessionalDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalDtoMapperTest {

    private final ProfessionalDtoMapper mapper = new ProfessionalDtoMapper();

    @Test
    @DisplayName("should map ProfessionalDto to domain correctly")
    void shouldMapToDomainCorrectly() {
        ProfessionalDto dto = new ProfessionalDto(
                10L,
                "Mariana",
                "123.456.789-00",
                "11999999999",
                "mariana@email.com"
        );

        Professional domain = mapper.toDomain(dto);

        assertThat(domain.id()).isEqualTo(10L);
        assertThat(domain.name()).isEqualTo("Mariana");
    }
}
