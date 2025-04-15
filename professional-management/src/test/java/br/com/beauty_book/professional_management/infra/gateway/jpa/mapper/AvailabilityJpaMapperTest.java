
package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.Availability;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.AvailabilityJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvailabilityJpaMapperTest {

    private final AvailabilityJpaMapper mapper = new AvailabilityJpaMapper();

    @Test
    void shouldMapToDomainCorrectly() {
        ProfessionalJpaEntity professional = new ProfessionalJpaEntity(1L, "Joana", "123.456.789-00", "11999999999", "joana@email.com");
        AvailabilityJpaEntity entity = new AvailabilityJpaEntity(10L, professional, 1, LocalTime.of(9, 0), LocalTime.of(17, 0));

        Availability domain = mapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.id()).isEqualTo(10L);
        assertThat(domain.dayOfWeek()).isEqualTo(1);
        assertThat(domain.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(domain.endTime()).isEqualTo(LocalTime.of(17, 0));
    }

    @Test
    void shouldMapToEntityCorrectly() {
        Availability domain = new Availability(null, 2, LocalTime.of(10, 0), LocalTime.of(18, 0));
        ProfessionalJpaEntity professional = new ProfessionalJpaEntity(2L, "Lucas", "987.654.321-00", "11988888888", "lucas@email.com");

        AvailabilityJpaEntity entity = mapper.toEntity(domain, professional);

        assertThat(entity).isNotNull();
        assertThat(entity.getProfessional()).isEqualTo(professional);
        assertThat(entity.getDayOfWeek()).isEqualTo(2);
        assertThat(entity.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(entity.getEndTime()).isEqualTo(LocalTime.of(18, 0));
    }
}
