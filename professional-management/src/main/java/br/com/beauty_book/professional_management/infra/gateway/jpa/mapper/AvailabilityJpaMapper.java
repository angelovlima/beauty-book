package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.Availability;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.AvailabilityJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityJpaMapper {

    public Availability toDomain(AvailabilityJpaEntity entity) {
        return new Availability(
                entity.getId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    public AvailabilityJpaEntity toEntity(Availability domain, ProfessionalJpaEntity professional) {
        return new AvailabilityJpaEntity(
                null,
                professional,
                domain.dayOfWeek(),
                domain.startTime(),
                domain.endTime()
        );
    }
}
