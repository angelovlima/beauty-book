package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferedJpaMapper {

    public ServiceOffered toDomain(ServiceOfferedJpaEntity entity) {
        return new ServiceOffered(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDurationMinutes()
        );
    }
}
