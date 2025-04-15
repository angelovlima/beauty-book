package br.com.beauty_book.booking.domain.service;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;

public interface NotificationService {
    void sendBookingCreated(CustomerDto customer, ProfessionalDto professional, Booking booking);
    void sendBookingCancelled(CustomerDto customer, ProfessionalDto professional, Booking booking);
    void sendBookingRescheduled(CustomerDto customer, ProfessionalDto professional, Booking booking);
}
