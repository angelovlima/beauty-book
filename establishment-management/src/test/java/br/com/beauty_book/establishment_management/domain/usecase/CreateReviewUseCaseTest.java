package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.NoCompletedBookingHistoryException;
import br.com.beauty_book.establishment_management.domain.exception.ReviewAlreadyExistsException;
import br.com.beauty_book.establishment_management.domain.gateway.*;
import br.com.beauty_book.establishment_management.domain.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateReviewUseCaseTest {

    private ReviewGateway reviewGateway;
    private CustomerGateway customerGateway;
    private EstablishmentGateway establishmentGateway;
    private BookingHistoryVerificationGateway bookingVerificationGateway;
    private CreateReviewUseCase useCase;

    @BeforeEach
    void setUp() {
        reviewGateway = mock(ReviewGateway.class);
        customerGateway = mock(CustomerGateway.class);
        establishmentGateway = mock(EstablishmentGateway.class);
        bookingVerificationGateway = mock(BookingHistoryVerificationGateway.class);
        useCase = new CreateReviewUseCase(
                reviewGateway,
                customerGateway,
                establishmentGateway,
                bookingVerificationGateway
        );
    }

    @Test
    void shouldCreateReviewSuccessfullyWhenValid() {
        Review review = new Review(1L, 100L, 200L, 5, "Ótimo atendimento");

        when(reviewGateway.findByCustomerIdAndEstablishmentId(100L, 200L)).thenReturn(Optional.empty());
        when(bookingVerificationGateway.customerHasCompletedBooking(100L, 200L)).thenReturn(true);
        when(reviewGateway.save(review)).thenReturn(review);

        Review result = useCase.execute(review);

        assertThat(result).isEqualTo(review);
        verify(customerGateway).findById(100L);
        verify(establishmentGateway).findById(200L);
        verify(reviewGateway).save(review);
    }

    @Test
    void shouldThrowExceptionWhenReviewAlreadyExists() {
        Review review = new Review(1L, 100L, 200L, 4, "Já avaliado");

        when(reviewGateway.findByCustomerIdAndEstablishmentId(100L, 200L))
                .thenReturn(Optional.of(review));

        assertThatThrownBy(() -> useCase.execute(review))
                .isInstanceOf(ReviewAlreadyExistsException.class)
                .hasMessageContaining("O cliente com ID 100 já avaliou o estabelecimento com ID 200.");
    }

    @Test
    void shouldThrowExceptionWhenNoCompletedBookingExists() {
        Review review = new Review(1L, 100L, 200L, 5, "Serviço bom");

        when(reviewGateway.findByCustomerIdAndEstablishmentId(100L, 200L)).thenReturn(Optional.empty());
        when(bookingVerificationGateway.customerHasCompletedBooking(100L, 200L)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(review))
                .isInstanceOf(NoCompletedBookingHistoryException.class)
                .hasMessageContaining("Cliente com ID 100 não possui histórico de atendimento COMPLETED no estabelecimento com ID 200.");
    }
}
