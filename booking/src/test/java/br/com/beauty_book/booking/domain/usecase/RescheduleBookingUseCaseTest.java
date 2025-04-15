package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.*;
import br.com.beauty_book.booking.domain.gateway.*;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RescheduleBookingUseCaseTest {

    private BookingGateway bookingGateway;
    private EstablishmentGateway establishmentGateway;
    private ProfessionalGateway professionalGateway;
    private BookingNotifierGateway notifierGateway;

    private RescheduleBookingUseCase useCase;

    @BeforeEach
    void setup() {
        bookingGateway = mock(BookingGateway.class);
        establishmentGateway = mock(EstablishmentGateway.class);
        professionalGateway = mock(ProfessionalGateway.class);
        notifierGateway = mock(BookingNotifierGateway.class);
        useCase = new RescheduleBookingUseCase(bookingGateway, establishmentGateway, professionalGateway, notifierGateway);
    }

    @Test
    void shouldRescheduleSuccessfully() {
        Booking booking = buildScheduledBooking();

        Booking updatedBooking = new Booking(
                1L, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                BookingStatus.RESCHEDULED
        );

        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0))));
        when(professionalGateway.getAvailabilityByProfessionalId(2L))
                .thenReturn(List.of(new AvailabilityDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0))));
        when(bookingGateway.findByProfessionalIdAndBookingDate(2L, LocalDate.of(2025, 4, 25)))
                .thenReturn(List.of());
        when(bookingGateway.update(any())).thenReturn(updatedBooking);

        Booking result = useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(14, 0));

        assertThat(result.getBookingDate()).isEqualTo(LocalDate.of(2025, 4, 25));
        assertThat(result.getStartTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(result.getEndTime()).isEqualTo(LocalTime.of(14, 30));
        assertThat(result.getStatus()).isEqualTo(BookingStatus.RESCHEDULED);

        verify(notifierGateway).notifyBookingRescheduled(result);
    }

    @Test
    void shouldThrowIfBookingNotFound() {
        when(bookingGateway.findById(1L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.now(), LocalTime.of(10, 0)));

        assertThat(thrown)
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessage("Agendamento com ID 1 não encontrado.");
    }

    @Test
    void shouldThrowIfBookingCancelled() {
        Booking booking = buildScheduledBooking(BookingStatus.CANCELLED);
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.now(), LocalTime.of(11, 0)));

        assertThat(thrown)
                .isInstanceOf(BookingAlreadyCancelledException.class)
                .hasMessage("Agendamento já está cancelado.");
    }

    @Test
    void shouldThrowIfBookingCompleted() {
        Booking booking = buildScheduledBooking(BookingStatus.COMPLETED);
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.now(), LocalTime.of(11, 0)));

        assertThat(thrown)
                .isInstanceOf(CompletedBookingCannotBeCancelledException.class)
                .hasMessage("Não é possível reagendar um agendamento finalizado.");
    }

    @Test
    void shouldThrowIfEstablishmentClosedThatDay() {
        Booking booking = buildScheduledBooking();
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(List.of());

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(10, 0)));

        assertThat(thrown)
                .isInstanceOf(EstablishmentNotAvailableException.class)
                .hasMessage("Estabelecimento não funciona neste dia da semana.");
    }

    @Test
    void shouldThrowIfEstablishmentClosedThatHour() {
        Booking booking = buildScheduledBooking();
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(10, 0))));

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(11, 0)));

        assertThat(thrown)
                .isInstanceOf(EstablishmentNotAvailableException.class)
                .hasMessage("Estabelecimento não está disponível neste horário.");
    }

    @Test
    void shouldThrowIfProfessionalUnavailableOnDay() {
        Booking booking = buildScheduledBooking();
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0))));
        when(professionalGateway.getAvailabilityByProfessionalId(2L)).thenReturn(List.of());

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(10, 0)));

        assertThat(thrown)
                .isInstanceOf(ProfessionalNotAvailableException.class)
                .hasMessage("Profissional não atende neste dia.");
    }

    @Test
    void shouldThrowIfProfessionalUnavailableThatHour() {
        Booking booking = buildScheduledBooking();
        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0))));
        when(professionalGateway.getAvailabilityByProfessionalId(2L))
                .thenReturn(List.of(new AvailabilityDto(5, LocalTime.of(9, 0), LocalTime.of(10, 0))));

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(11, 0)));

        assertThat(thrown)
                .isInstanceOf(ProfessionalNotAvailableException.class)
                .hasMessage("Profissional não está disponível neste horário.");
    }

    @Test
    void shouldThrowIfConflictExists() {
        Booking booking = buildScheduledBooking();
        Booking conflict = new Booking(99L, 9L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(13, 30),
                LocalTime.of(14, 30),
                BookingStatus.SCHEDULED
        );

        when(bookingGateway.findById(1L)).thenReturn(Optional.of(booking));
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L))
                .thenReturn(List.of(new OperatingHourDto(5, LocalTime.of(8, 0), LocalTime.of(18, 0))));
        when(professionalGateway.getAvailabilityByProfessionalId(2L))
                .thenReturn(List.of(new AvailabilityDto(5, LocalTime.of(9, 0), LocalTime.of(18, 0))));
        when(bookingGateway.findByProfessionalIdAndBookingDate(2L, LocalDate.of(2025, 4, 25)))
                .thenReturn(List.of(conflict));

        Throwable thrown = catchThrowable(() -> useCase.execute(1L, LocalDate.of(2025, 4, 25), LocalTime.of(14, 0)));

        assertThat(thrown)
                .isInstanceOf(BookingConflictException.class)
                .hasMessage("Horário conflitante com outro agendamento.");
    }

    private Booking buildScheduledBooking() {
        return buildScheduledBooking(BookingStatus.SCHEDULED);
    }

    private Booking buildScheduledBooking(BookingStatus status) {
        return new Booking(
                1L, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 20),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                status
        );
    }
}
