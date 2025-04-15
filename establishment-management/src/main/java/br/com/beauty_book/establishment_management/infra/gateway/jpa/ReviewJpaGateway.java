package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.gateway.ReviewGateway;
import br.com.beauty_book.establishment_management.domain.model.Review;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.ReviewJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewJpaGateway implements ReviewGateway {

    private final ReviewRepository reviewRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ReviewJpaMapper mapper;

    @Override
    public Review save(Review review) {
        EstablishmentJpaEntity establishment = establishmentRepository.getReferenceById(review.getEstablishmentId());
        return mapper.toDomain(reviewRepository.save(mapper.toEntity(review, establishment)));
    }

    @Override
    public Optional<Review> findByCustomerIdAndEstablishmentId(Long customerId, Long establishmentId) {
        return reviewRepository.findByCustomerIdAndEstablishment_Id(customerId, establishmentId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Review> findAllByEstablishmentId(Long establishmentId) {
        return reviewRepository.findAllByEstablishment_Id(establishmentId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
