package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingVerificationUseCaseTest {

    private BookingGateway bookingGateway;
    private BookingVerificationUseCase useCase;

    @BeforeEach
    void setup() {
        bookingGateway = mock(BookingGateway.class);
        useCase = new BookingVerificationUseCase(bookingGateway);
    }

    @Test
    void shouldReturnTrueIfCompletedBookingExists() {
        when(bookingGateway.existsByCustomerIdAndEstablishmentIdAndStatus(1L, 2L, br.com.beauty_book.booking.domain.model.enums.BookingStatus.COMPLETED))
                .thenReturn(true);

        boolean result = useCase.hasCompletedBookingByCustomerAndEstablishment(1L, 2L);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseIfNoCompletedBookingExists() {
        when(bookingGateway.existsByCustomerIdAndEstablishmentIdAndStatus(1L, 2L, br.com.beauty_book.booking.domain.model.enums.BookingStatus.COMPLETED))
                .thenReturn(false);

        boolean result = useCase.hasCompletedBookingByCustomerAndEstablishment(1L, 2L);

        assertThat(result).isFalse();
    }
}
