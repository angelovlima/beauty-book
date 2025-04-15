package br.com.beauty_book.booking.infra.gateway;

import br.com.beauty_book.booking.domain.exception.NotificationCustomerNotFoundException;
import br.com.beauty_book.booking.domain.exception.NotificationProfessionalNotFoundException;
import br.com.beauty_book.booking.domain.gateway.CustomerGateway;
import br.com.beauty_book.booking.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.domain.service.NotificationService;
import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class BookingNotifierGatewayImplTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private ProfessionalGateway professionalGateway;

    @InjectMocks
    private BookingNotifierGatewayImpl notifier;

    private Booking booking;

    private final Long customerId = 1L;
    private final Long professionalId = 2L;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        booking = new Booking(
                100L,
                customerId,
                professionalId,
                10L,
                20L,
                LocalDate.now(),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                BookingStatus.SCHEDULED
        );
    }

    @Test
    void shouldNotifyBookingCreated() {
        CustomerDto customer = new CustomerDto(customerId, "Carlos", "carlos@email.com");
        ProfessionalDto professional = new ProfessionalDto(professionalId, "Ana", "123.456.789-00", "11999999999", "ana@email.com");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(professionalGateway.findById(professionalId)).thenReturn(Optional.of(professional));

        notifier.notifyBookingCreated(booking);

        verify(notificationService).sendBookingCreated(customer, professional, booking);
    }

    @Test
    void shouldNotifyBookingCancelled() {
        CustomerDto customer = new CustomerDto(customerId, "Carlos", "carlos@email.com");
        ProfessionalDto professional = new ProfessionalDto(professionalId, "Ana", "123.456.789-00", "11999999999", "ana@email.com");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(professionalGateway.findById(professionalId)).thenReturn(Optional.of(professional));

        notifier.notifyBookingCancelled(booking);

        verify(notificationService).sendBookingCancelled(customer, professional, booking);
    }

    @Test
    void shouldNotifyBookingRescheduled() {
        CustomerDto customer = new CustomerDto(customerId, "Carlos", "carlos@email.com");
        ProfessionalDto professional = new ProfessionalDto(professionalId, "Ana", "123.456.789-00", "11999999999", "ana@email.com");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(professionalGateway.findById(professionalId)).thenReturn(Optional.of(professional));

        notifier.notifyBookingRescheduled(booking);

        verify(notificationService).sendBookingRescheduled(customer, professional, booking);
    }

    @Test
    void shouldThrowNotificationCustomerNotFoundException() {
        when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notifier.notifyBookingCreated(booking))
                .isInstanceOf(NotificationCustomerNotFoundException.class)
                .hasMessageContaining("ID " + customerId);
    }

    @Test
    void shouldThrowNotificationProfessionalNotFoundException() {
        CustomerDto customer = new CustomerDto(customerId, "Carlos", "carlos@email.com");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(professionalGateway.findById(professionalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notifier.notifyBookingCreated(booking))
                .isInstanceOf(NotificationProfessionalNotFoundException.class)
                .hasMessageContaining("ID " + professionalId);
    }
}
