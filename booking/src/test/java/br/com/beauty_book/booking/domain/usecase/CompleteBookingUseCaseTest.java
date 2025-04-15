package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingAlreadyCancelledException;
import br.com.beauty_book.booking.domain.exception.BookingAlreadyCompletedException;
import br.com.beauty_book.booking.domain.exception.BookingNotFoundException;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompleteBookingUseCaseTest {

    private BookingGateway gateway;
    private CompleteBookingUseCase useCase;

    @BeforeEach
    void setup() {
        gateway = mock(BookingGateway.class);
        useCase = new CompleteBookingUseCase(gateway);
    }

    @Test
    void shouldCompleteBookingSuccessfully() {
        Booking booking = new Booking(
                1L, 10L, 20L, 30L, 40L,
                LocalDate.of(2025, 4, 20),
                LocalTime.of(10, 0),
                LocalTime.of(10, 45),
                BookingStatus.SCHEDULED
        );

        when(gateway.findById(1L)).thenReturn(Optional.of(booking));
        when(gateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = useCase.execute(1L);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        verify(gateway).update(result);
    }

    @Test
    void shouldThrowWhenBookingNotFound() {
        when(gateway.findById(99L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> useCase.execute(99L));

        assertThat(thrown)
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessage("Agendamento com ID 99 não encontrado.");
    }

    @Test
    void shouldThrowWhenBookingAlreadyCancelled() {
        Booking booking = new Booking(
                2L, 10L, 20L, 30L, 40L,
                LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(9, 30),
                BookingStatus.CANCELLED
        );

        when(gateway.findById(2L)).thenReturn(Optional.of(booking));

        Throwable thrown = catchThrowable(() -> useCase.execute(2L));

        assertThat(thrown)
                .isInstanceOf(BookingAlreadyCancelledException.class)
                .hasMessage("Não é possível finalizar um agendamento que foi cancelado.");
    }

    @Test
    void shouldThrowWhenBookingAlreadyCompleted() {
        Booking booking = new Booking(
                3L, 10L, 20L, 30L, 40L,
                LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(9, 30),
                BookingStatus.COMPLETED
        );

        when(gateway.findById(3L)).thenReturn(Optional.of(booking));

        Throwable thrown = catchThrowable(() -> useCase.execute(3L));

        assertThat(thrown)
                .isInstanceOf(BookingAlreadyCompletedException.class)
                .hasMessage("Agendamento já está finalizado.");
    }
}
