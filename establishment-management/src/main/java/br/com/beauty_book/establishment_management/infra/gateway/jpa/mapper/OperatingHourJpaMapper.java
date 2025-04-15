package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.OperatingHourJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OperatingHourJpaMapper {

    public OperatingHour toDomain(OperatingHourJpaEntity entity) {
        return new OperatingHour(
                entity.getId(),
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}
