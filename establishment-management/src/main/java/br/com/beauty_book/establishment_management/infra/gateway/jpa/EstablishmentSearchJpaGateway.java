package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentSearchGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.EstablishmentJpaMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EstablishmentSearchJpaGateway implements EstablishmentSearchGateway {

    private final EstablishmentJpaMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Establishment> search(EstablishmentSearchFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EstablishmentJpaEntity> query = cb.createQuery(EstablishmentJpaEntity.class);
        Root<EstablishmentJpaEntity> root = query.from(EstablishmentJpaEntity.class);

        Join<Object, Object> servicesJoin = root.join("services", JoinType.LEFT);
        Join<Object, Object> hoursJoin = root.join("operatingHours", JoinType.LEFT);
        Join<Object, Object> reviewsJoin = root.join("reviews", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.name() != null && !filter.name().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
        }

        if (filter.location() != null && !filter.location().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("address")), "%" + filter.location().toLowerCase() + "%"));
        }

        if (filter.serviceName() != null && !filter.serviceName().isBlank()) {
            predicates.add(cb.like(cb.lower(servicesJoin.get("name")), "%" + filter.serviceName().toLowerCase() + "%"));
        }

        if (filter.dayOfWeekAvailable() != null) {
            predicates.add(cb.equal(hoursJoin.get("dayOfWeek"), filter.dayOfWeekAvailable()));
        }

        if (filter.minPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(servicesJoin.get("price"), filter.minPrice()));
        }

        if (filter.maxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(servicesJoin.get("price"), filter.maxPrice()));
        }

        if (filter.minRating() != null) {
            Expression<Double> avgRating = cb.avg(reviewsJoin.get("stars"));
            query.groupBy(root.get("id"));
            query.having(cb.greaterThanOrEqualTo(avgRating, filter.minRating().doubleValue()));
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);

        return entityManager.createQuery(query).getResultList().stream()
                .map(entity -> mapper.toDomain(entity))
                .toList();
    }
}
