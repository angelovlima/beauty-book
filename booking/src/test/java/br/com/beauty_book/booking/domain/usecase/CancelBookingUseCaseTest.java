package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingAlreadyCancelledException;
import br.com.beauty_book.booking.domain.exception.BookingNotFoundException;
import br.com.beauty_book.booking.domain.exception.CompletedBookingCannotBeCancelledException;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.gateway.BookingNotifierGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelBookingUseCaseTest {

    private BookingGateway bookingGateway;
    private BookingNotifierGateway notifierGateway;
    private CancelBookingUseCase useCase;

    @BeforeEach
    void setUp() {
        bookingGateway = mock(BookingGateway.class);
        notifierGateway = mock(BookingNotifierGateway.class);
        useCase = new CancelBookingUseCase(bookingGateway, notifierGateway);
    }

    @Test
    void shouldCancelBookingSuccessfully() {
        Booking existing = new Booking(
                10L, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 20),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                BookingStatus.SCHEDULED
        );

        when(bookingGateway.findById(10L)).thenReturn(Optional.of(existing));
        when(bookingGateway.update(any())).thenReturn(existing);

        Booking result = useCase.execute(10L);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        verify(bookingGateway).update(result);
        verify(notifierGateway).notifyBookingCancelled(result);
    }

    @Test
    void shouldThrowIfBookingAlreadyCancelled() {
        Booking cancelled = new Booking(
                11L, 1L, 2L, 3L, 4L,
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                BookingStatus.CANCELLED
        );

        when(bookingGateway.findById(11L)).thenReturn(Optional.of(cancelled));

        assertThatThrownBy(() -> useCase.execute(11L))
                .isInstanceOf(BookingAlreadyCancelledException.class)
                .hasMessageContaining("Agendamento já está cancelado");
    }

    @Test
    void shouldThrowIfBookingIsCompleted() {
        Booking completed = new Booking(
                12L, 1L, 2L, 3L, 4L,
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                BookingStatus.COMPLETED
        );

        when(bookingGateway.findById(12L)).thenReturn(Optional.of(completed));

        assertThatThrownBy(() -> useCase.execute(12L))
                .isInstanceOf(CompletedBookingCannotBeCancelledException.class)
                .hasMessageContaining("Não é possível cancelar");
    }

    @Test
    void shouldThrowIfBookingNotFound() {
        when(bookingGateway.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("Agendamento com ID 99 não encontrado");
    }
}
