package br.com.beauty_book.booking.infra.service;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.service.NotificationService;
import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleNotificationService implements NotificationService {

    @Override
    public void sendBookingCreated(CustomerDto customer, ProfessionalDto professional, Booking booking) {
        log.info("📩 [EMAIL] Booking CREATED (ID: {}) sent to customer <{}> and professional <{}>",
                booking.getId(), customer.email(), professional.email());
    }

    @Override
    public void sendBookingCancelled(CustomerDto customer, ProfessionalDto professional, Booking booking) {
        log.info("📩 [EMAIL] Booking CANCELLED (ID: {}) sent to customer <{}> and professional <{}>",
                booking.getId(), customer.email(), professional.email());
    }

    @Override
    public void sendBookingRescheduled(CustomerDto customer, ProfessionalDto professional, Booking booking) {
        log.info("📩 [EMAIL] Booking RESCHEDULED (ID: {}) sent to customer <{}> and professional <{}>",
                booking.getId(), customer.email(), professional.email());
    }
}
