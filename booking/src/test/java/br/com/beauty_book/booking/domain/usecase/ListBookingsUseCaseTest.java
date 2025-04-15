package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.api.dto.ListBookingsFilter;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ListBookingsUseCaseTest {

    private BookingGateway gateway;
    private ListBookingsUseCase useCase;

    @BeforeEach
    void setup() {
        gateway = mock(BookingGateway.class);
        useCase = new ListBookingsUseCase(gateway);
    }

    @Test
    void shouldReturnFilteredBookings() {
        ListBookingsFilter filter = new ListBookingsFilter(
                1L, 2L, 3L, LocalDate.of(2025, 4, 20)
        );

        List<Booking> expected = List.of(
                new Booking(1L, 1L, 2L, 3L, 4L, LocalDate.of(2025, 4, 20),
                        LocalTime.of(10, 0), LocalTime.of(10, 30), BookingStatus.SCHEDULED)
        );

        when(gateway.findAllWithFilters(1L, 2L, 3L, LocalDate.of(2025, 4, 20)))
                .thenReturn(expected);

        List<Booking> result = useCase.execute(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void shouldReturnEmptyListIfNoMatches() {
        ListBookingsFilter filter = new ListBookingsFilter(
                999L, null, null, null
        );

        when(gateway.findAllWithFilters(999L, null, null, null))
                .thenReturn(List.of());

        List<Booking> result = useCase.execute(filter);

        assertThat(result).isEmpty();
    }
}
