package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.Review;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewJpaMapper {

    public Review toDomain(ReviewJpaEntity entity) {
        return new Review(
                entity.getId(),
                entity.getCustomerId(),
                entity.getEstablishment().getId(),
                entity.getStars(),
                entity.getComment()
        );
    }

    public ReviewJpaEntity toEntity(Review review, EstablishmentJpaEntity establishment) {
        return new ReviewJpaEntity(
                review.getCustomerId(),
                establishment,
                review.getStars(),
                review.getComment()
        );
    }
}
