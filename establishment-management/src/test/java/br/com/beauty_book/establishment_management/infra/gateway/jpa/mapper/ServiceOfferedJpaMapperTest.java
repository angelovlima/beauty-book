package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceOfferedJpaMapperTest {

    private final ServiceOfferedJpaMapper mapper = new ServiceOfferedJpaMapper();

    @Test
    @DisplayName("should map ServiceOfferedJpaEntity to ServiceOffered domain correctly")
    void shouldMapToDomainCorrectly() {
        var establishment = new EstablishmentJpaEntity("Studio A", "Rua A", "https://example.com/foto.jpg");
        var entity = new ServiceOfferedJpaEntity("Corte", "Corte feminino", new BigDecimal("50.00"), 45, establishment);
        ReflectionTestUtils.setField(entity, "id", 5L);

        ServiceOffered domain = mapper.toDomain(entity);

        assertThat(domain.id()).isEqualTo(5L);
        assertThat(domain.name()).isEqualTo("Corte");
        assertThat(domain.description()).isEqualTo("Corte feminino");
        assertThat(domain.price()).isEqualTo(new BigDecimal("50.00"));
        assertThat(domain.durationMinutes()).isEqualTo(45);
    }
}
