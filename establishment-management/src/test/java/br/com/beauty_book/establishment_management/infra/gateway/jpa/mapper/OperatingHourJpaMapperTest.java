package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.OperatingHourJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class OperatingHourJpaMapperTest {

    private final OperatingHourJpaMapper mapper = new OperatingHourJpaMapper();

    @Test
    @DisplayName("should map OperatingHourJpaEntity to OperatingHour domain correctly")
    void shouldMapToDomainCorrectly() {
        var entity = new OperatingHourJpaEntity(
                2,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                new EstablishmentJpaEntity("Studio A", "Rua 1", "https://example.com/image.jpg")
        );
        ReflectionTestUtils.setField(entity, "id", 1L);

        OperatingHour domain = mapper.toDomain(entity);

        assertThat(domain.id()).isEqualTo(1L);
        assertThat(domain.dayOfWeek()).isEqualTo(2);
        assertThat(domain.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(domain.endTime()).isEqualTo(LocalTime.of(18, 0));
    }
}
