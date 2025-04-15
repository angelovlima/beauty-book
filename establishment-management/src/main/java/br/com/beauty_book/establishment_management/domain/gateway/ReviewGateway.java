package br.com.beauty_book.establishment_management.domain.gateway;

import br.com.beauty_book.establishment_management.domain.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewGateway {

    Review save(Review review);

    Optional<Review> findByCustomerIdAndEstablishmentId(Long customerId, Long establishmentId);

    List<Review> findAllByEstablishmentId(Long establishmentId);
}
