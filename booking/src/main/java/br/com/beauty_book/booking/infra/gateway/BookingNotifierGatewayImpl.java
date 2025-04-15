package br.com.beauty_book.booking.infra.gateway;

import br.com.beauty_book.booking.domain.exception.NotificationCustomerNotFoundException;
import br.com.beauty_book.booking.domain.exception.NotificationProfessionalNotFoundException;
import br.com.beauty_book.booking.domain.gateway.BookingNotifierGateway;
import br.com.beauty_book.booking.domain.gateway.CustomerGateway;
import br.com.beauty_book.booking.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.service.NotificationService;
import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingNotifierGatewayImpl implements BookingNotifierGateway {

    private final NotificationService notificationService;
    private final CustomerGateway customerGateway;
    private final ProfessionalGateway professionalGateway;

    @Override
    public void notifyBookingCreated(Booking booking) {
        var context = resolveNotificationContext(booking);
        notificationService.sendBookingCreated(context.customer(), context.professional(), booking);
    }

    @Override
    public void notifyBookingCancelled(Booking booking) {
        var context = resolveNotificationContext(booking);
        notificationService.sendBookingCancelled(context.customer(), context.professional(), booking);
    }

    @Override
    public void notifyBookingRescheduled(Booking booking) {
        var context = resolveNotificationContext(booking);
        notificationService.sendBookingRescheduled(context.customer(), context.professional(), booking);
    }

    private NotificationContext resolveNotificationContext(Booking booking) {
        CustomerDto customer = customerGateway.findById(booking.getCustomerId())
                .orElseThrow(() -> new NotificationCustomerNotFoundException(booking.getCustomerId()));

        ProfessionalDto professional = professionalGateway.findById(booking.getProfessionalId())
                .orElseThrow(() -> new NotificationProfessionalNotFoundException(booking.getProfessionalId()));

        return new NotificationContext(customer, professional);
    }

    private record NotificationContext(CustomerDto customer, ProfessionalDto professional) {}
}
