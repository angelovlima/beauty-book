package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.CreateReviewApiRequest;
import br.com.beauty_book.establishment_management.api.dto.ReviewApiResponse;
import br.com.beauty_book.establishment_management.domain.model.Review;

public class ReviewApiMapper {

    private ReviewApiMapper() {}

    public static Review toDomain(CreateReviewApiRequest request) {
        return new Review(
                null,
                request.customerId(),
                request.establishmentId(),
                request.stars(),
                request.comment()
        );
    }

    public static ReviewApiResponse toResponse(Review review) {
        return new ReviewApiResponse(
                review.getId(),
                review.getCustomerId(),
                review.getEstablishmentId(),
                review.getStars(),
                review.getComment()
        );
    }
}
