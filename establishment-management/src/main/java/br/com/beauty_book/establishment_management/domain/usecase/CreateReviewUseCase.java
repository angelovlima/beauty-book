package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.NoCompletedBookingHistoryException;
import br.com.beauty_book.establishment_management.domain.exception.ReviewAlreadyExistsException;
import br.com.beauty_book.establishment_management.domain.gateway.BookingHistoryVerificationGateway;
import br.com.beauty_book.establishment_management.domain.gateway.CustomerGateway;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.gateway.ReviewGateway;
import br.com.beauty_book.establishment_management.domain.model.Review;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateReviewUseCase {

    private final ReviewGateway reviewGateway;
    private final CustomerGateway customerGateway;
    private final EstablishmentGateway establishmentGateway;
    private final BookingHistoryVerificationGateway bookingVerificationGateway;

    public CreateReviewUseCase(
            ReviewGateway reviewGateway,
            CustomerGateway customerGateway,
            EstablishmentGateway establishmentGateway,
            BookingHistoryVerificationGateway bookingVerificationGateway
    ) {
        this.reviewGateway = Objects.requireNonNull(reviewGateway);
        this.customerGateway = Objects.requireNonNull(customerGateway);
        this.establishmentGateway = Objects.requireNonNull(establishmentGateway);
        this.bookingVerificationGateway = Objects.requireNonNull(bookingVerificationGateway);
    }

    public Review execute(Review review) {

        customerGateway.findById(review.getCustomerId());

        establishmentGateway.findById(review.getEstablishmentId());

        reviewGateway.findByCustomerIdAndEstablishmentId(
                review.getCustomerId(),
                review.getEstablishmentId()
        ).ifPresent(existing -> {
            throw new ReviewAlreadyExistsException(review.getCustomerId(), review.getEstablishmentId());
        });

        boolean hasCompleted = bookingVerificationGateway.customerHasCompletedBooking(
                review.getCustomerId(),
                review.getEstablishmentId()
        );

        if (!hasCompleted) {
            throw new NoCompletedBookingHistoryException(review.getCustomerId(), review.getEstablishmentId());
        }

        return reviewGateway.save(review);
    }
}
