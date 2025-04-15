package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.*;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstablishmentJpaMapper {

    public static Establishment toDomain(EstablishmentJpaEntity entity) {
        return new Establishment(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhotoUrl(),
                toOperatingHourDomainList(entity.getOperatingHours()),
                toServiceDomainList(entity.getServices()),
                toProfessionalDomainList(entity.getProfessionals())
        );
    }

    public static EstablishmentJpaEntity toEntity(Establishment domain) {
        EstablishmentJpaEntity entity = new EstablishmentJpaEntity(
                domain.getName(),
                domain.getAddress(),
                domain.getPhotoUrl()
        );

        entity.getOperatingHours().addAll(
                domain.getOperatingHours().stream()
                        .map(o -> new OperatingHourJpaEntity(o.dayOfWeek(), o.startTime(), o.endTime(), entity)).toList()
        );

        entity.getServices().addAll(
                domain.getServices().stream()
                        .map(s -> new ServiceOfferedJpaEntity(
                                s.name(),
                                s.description(),
                                s.price(),
                                s.durationMinutes(),
                                entity
                        )).toList()
        );

        entity.getProfessionals().addAll(
                domain.getProfessionals().stream()
                        .map(p -> new ProfessionalEstablishmentJpaEntity(
                                p.professionalId(),
                                entity
                        )).toList()
        );

        return entity;
    }

    private static List<OperatingHour> toOperatingHourDomainList(List<OperatingHourJpaEntity> entities) {
        return entities.stream()
                .map(e -> new OperatingHour(e.getId(), e.getDayOfWeek(), e.getStartTime(), e.getEndTime()))
                .collect(Collectors.toList());
    }

    private static List<ServiceOffered> toServiceDomainList(List<ServiceOfferedJpaEntity> entities) {
        return entities.stream()
                .map(e -> new ServiceOffered(e.getId(), e.getName(), e.getDescription(), e.getPrice(), e.getDurationMinutes()))
                .collect(Collectors.toList());
    }

    private static List<ProfessionalEstablishment> toProfessionalDomainList(List<ProfessionalEstablishmentJpaEntity> entities) {
        return entities.stream()
                .map(e -> new ProfessionalEstablishment(e.getId(), e.getProfessionalId()))
                .collect(Collectors.toList());
    }
}
