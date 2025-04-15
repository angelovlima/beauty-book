package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.*;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.OperatingHourJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ProfessionalEstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EstablishmentJpaMapperTest {

    @Test
    @DisplayName("should map Establishment domain to EstablishmentJpaEntity correctly")
    void shouldMapToEntityCorrectly() {
        Establishment domain = new Establishment(
                null,
                "Studio A",
                "Av. Central",
                "https://example.com/image.jpg",
                List.of(new OperatingHour(null, 1, LocalTime.of(8, 0), LocalTime.of(18, 0))),
                List.of(new ServiceOffered(null, "Corte", "Corte feminino", new BigDecimal("50.00"), 45)),
                List.of(new ProfessionalEstablishment(null, 10L))
        );

        EstablishmentJpaEntity entity = EstablishmentJpaMapper.toEntity(domain);

        assertThat(entity.getName()).isEqualTo("Studio A");
        assertThat(entity.getAddress()).isEqualTo("Av. Central");
        assertThat(entity.getPhotoUrl()).isEqualTo("https://example.com/image.jpg");

        assertThat(entity.getOperatingHours()).hasSize(1);
        OperatingHourJpaEntity hour = entity.getOperatingHours().get(0);
        assertThat(hour.getDayOfWeek()).isEqualTo(1);
        assertThat(hour.getStartTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(hour.getEndTime()).isEqualTo(LocalTime.of(18, 0));

        assertThat(entity.getServices()).hasSize(1);
        ServiceOfferedJpaEntity service = entity.getServices().get(0);
        assertThat(service.getName()).isEqualTo("Corte");
        assertThat(service.getDescription()).isEqualTo("Corte feminino");
        assertThat(service.getPrice()).isEqualTo(new BigDecimal("50.00"));
        assertThat(service.getDurationMinutes()).isEqualTo(45);

        assertThat(entity.getProfessionals()).hasSize(1);
        ProfessionalEstablishmentJpaEntity prof = entity.getProfessionals().get(0);
        assertThat(prof.getProfessionalId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("should map EstablishmentJpaEntity to Establishment domain correctly")
    void shouldMapToDomainCorrectly() {
        EstablishmentJpaEntity entity = new EstablishmentJpaEntity(
                "Studio A",
                "Av. Central",
                "https://example.com/image.jpg"
        );

        entity.getOperatingHours().add(new OperatingHourJpaEntity(1, LocalTime.of(8, 0), LocalTime.of(18, 0), entity));
        entity.getServices().add(new ServiceOfferedJpaEntity("Corte", "Corte feminino", new BigDecimal("50.00"), 45, entity));
        entity.getProfessionals().add(new ProfessionalEstablishmentJpaEntity(10L, entity));

        Establishment domain = EstablishmentJpaMapper.toDomain(entity);

        assertThat(domain.getName()).isEqualTo("Studio A");
        assertThat(domain.getAddress()).isEqualTo("Av. Central");
        assertThat(domain.getPhotoUrl()).isEqualTo("https://example.com/image.jpg");

        assertThat(domain.getOperatingHours()).hasSize(1);
        OperatingHour hour = domain.getOperatingHours().get(0);
        assertThat(hour.dayOfWeek()).isEqualTo(1);
        assertThat(hour.startTime()).isEqualTo(LocalTime.of(8, 0));
        assertThat(hour.endTime()).isEqualTo(LocalTime.of(18, 0));

        assertThat(domain.getServices()).hasSize(1);
        ServiceOffered service = domain.getServices().get(0);
        assertThat(service.name()).isEqualTo("Corte");
        assertThat(service.description()).isEqualTo("Corte feminino");
        assertThat(service.price()).isEqualTo(new BigDecimal("50.00"));
        assertThat(service.durationMinutes()).isEqualTo(45);

        assertThat(domain.getProfessionals()).hasSize(1);
        ProfessionalEstablishment prof = domain.getProfessionals().get(0);
        assertThat(prof.professionalId()).isEqualTo(10L);
    }
}
