package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.ReviewGateway;
import br.com.beauty_book.establishment_management.domain.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListReviewsByEstablishmentUseCase {

    private final ReviewGateway reviewGateway;

    public ListReviewsByEstablishmentUseCase(ReviewGateway reviewGateway) {
        this.reviewGateway = reviewGateway;
    }

    public List<Review> execute(Long establishmentId) {
        return reviewGateway.findAllByEstablishmentId(establishmentId);
    }
}
