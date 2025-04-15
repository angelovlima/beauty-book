package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.ServiceOfferedApiResponse;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceOfferedApiMapperTest {

    @Test
    void shouldMapDomainToResponse() {
        ServiceOffered domain = new ServiceOffered(
                1L,
                "Corte Feminino",
                "Corte com hidratação",
                new BigDecimal("80.00"),
                45
        );

        ServiceOfferedApiMapper mapper = new ServiceOfferedApiMapper();

        ServiceOfferedApiResponse response = mapper.toResponse(domain);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Corte Feminino");
        assertThat(response.description()).isEqualTo("Corte com hidratação");
        assertThat(response.price()).isEqualByComparingTo("80.00");
        assertThat(response.durationMinutes()).isEqualTo(45);
    }
}
