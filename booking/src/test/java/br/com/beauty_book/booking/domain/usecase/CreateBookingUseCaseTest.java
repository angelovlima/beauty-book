package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingConflictException;
import br.com.beauty_book.booking.domain.exception.EstablishmentNotAvailableException;
import br.com.beauty_book.booking.domain.exception.ProfessionalNotAvailableException;
import br.com.beauty_book.booking.domain.gateway.*;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateBookingUseCaseTest {

    private BookingGateway bookingGateway;
    private CustomerGateway customerGateway;
    private ProfessionalGateway professionalGateway;
    private EstablishmentGateway establishmentGateway;
    private BookingNotifierGateway notifierGateway;

    private CreateBookingUseCase useCase;

    @BeforeEach
    void setup() {
        bookingGateway = mock(BookingGateway.class);
        customerGateway = mock(CustomerGateway.class);
        professionalGateway = mock(ProfessionalGateway.class);
        establishmentGateway = mock(EstablishmentGateway.class);
        notifierGateway = mock(BookingNotifierGateway.class);

        useCase = new CreateBookingUseCase(
                bookingGateway,
                customerGateway,
                professionalGateway,
                establishmentGateway,
                notifierGateway
        );
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(14, 0),
                null,
                BookingStatus.SCHEDULED
        );

        when(customerGateway.findById(1L)).thenReturn(mock());
        when(professionalGateway.findById(2L)).thenReturn(mock());
        when(establishmentGateway.findById(3L)).thenReturn(
                new EstablishmentDto(3L, "Studio X", "Av. Paulista", "url.jpg")
        );
        when(establishmentGateway.getServiceById(4L)).thenReturn(
                new ServiceOfferedDto(4L, 3L, "Corte", "Corte feminino", new BigDecimal("50.00"), 30)
        );
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(30);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(
                List.of(new OperatingHourDto(5, LocalTime.of(9, 0), LocalTime.of(18, 0)))
        );
        when(professionalGateway.getAvailabilityByProfessionalId(2L)).thenReturn(
                List.of(new AvailabilityDto(5, LocalTime.of(8, 0), LocalTime.of(17, 0)))
        );
        when(bookingGateway.findByProfessionalIdAndBookingDate(2L, LocalDate.of(2025, 4, 25)))
                .thenReturn(List.of());

        Booking saved = new Booking(
                100L, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                BookingStatus.SCHEDULED
        );

        when(bookingGateway.save(any())).thenReturn(saved);

        Booking result = useCase.execute(booking);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.SCHEDULED);
        assertThat(result.getEndTime()).isEqualTo(LocalTime.of(14, 30));

        verify(notifierGateway).notifyBookingCreated(result);
    }

    @Test
    void shouldThrowIfEstablishmentClosedOnDay() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 27),
                LocalTime.of(10, 0),
                null,
                BookingStatus.SCHEDULED
        );

        when(customerGateway.findById(any())).thenReturn(mock());
        when(professionalGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceById(any())).thenReturn(mock());
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(List.of());

        Throwable thrown = catchThrowable(() -> useCase.execute(booking));

        assertThat(thrown)
                .isInstanceOf(EstablishmentNotAvailableException.class)
                .hasMessage("Estabelecimento não funciona neste dia da semana.");
    }

    @Test
    void shouldThrowIfEstablishmentClosedAtTime() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(22, 0),
                null,
                BookingStatus.SCHEDULED
        );

        when(customerGateway.findById(any())).thenReturn(mock());
        when(professionalGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceById(any())).thenReturn(mock());
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(
                List.of(new OperatingHourDto(5, LocalTime.of(9, 0), LocalTime.of(18, 0)))
        );

        Throwable thrown = catchThrowable(() -> useCase.execute(booking));

        assertThat(thrown)
                .isInstanceOf(EstablishmentNotAvailableException.class)
                .hasMessage("Estabelecimento está fechado nesse horário.");
    }

    @Test
    void shouldThrowIfProfessionalUnavailable() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(16, 0),
                null,
                BookingStatus.SCHEDULED
        );

        when(customerGateway.findById(any())).thenReturn(mock());
        when(professionalGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(45);
        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(
                List.of(new OperatingHourDto(5, LocalTime.of(9, 0), LocalTime.of(18, 0)))
        );
        when(professionalGateway.getAvailabilityByProfessionalId(2L)).thenReturn(List.of());

        Throwable thrown = catchThrowable(() -> useCase.execute(booking));

        assertThat(thrown)
                .isInstanceOf(ProfessionalNotAvailableException.class)
                .hasMessage("Profissional não atende neste dia da semana.");
    }

    @Test
    void shouldThrowIfBookingConflictExists() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(10, 0),
                null,
                BookingStatus.SCHEDULED
        );

        when(customerGateway.findById(any())).thenReturn(mock());
        when(professionalGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.findById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceById(any())).thenReturn(mock());
        when(establishmentGateway.getServiceDuration(4L)).thenReturn(60);

        when(establishmentGateway.getOperatingHoursByEstablishmentId(3L)).thenReturn(
                List.of(new OperatingHourDto(5, LocalTime.of(9, 0), LocalTime.of(18, 0)))
        );
        when(professionalGateway.getAvailabilityByProfessionalId(2L)).thenReturn(
                List.of(new AvailabilityDto(5, LocalTime.of(9, 0), LocalTime.of(17, 0)))
        );
        when(bookingGateway.findByProfessionalIdAndBookingDate(2L, booking.getBookingDate()))
                .thenReturn(List.of(new Booking(
                        50L, 10L, 2L, 3L, 4L,
                        booking.getBookingDate(),
                        LocalTime.of(9, 30),
                        LocalTime.of(10, 30),
                        BookingStatus.SCHEDULED
                )));

        Throwable thrown = catchThrowable(() -> useCase.execute(booking));

        assertThat(thrown)
                .isInstanceOf(BookingConflictException.class)
                .hasMessage("Já existe um agendamento conflitante para esse horário.");
    }
}
