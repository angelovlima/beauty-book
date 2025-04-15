package br.com.beauty_book.booking.domain.gateway;

import br.com.beauty_book.booking.domain.model.Booking;

public interface BookingNotifierGateway {

    void notifyBookingCreated(Booking booking);

    void notifyBookingCancelled(Booking booking);

    void notifyBookingRescheduled(Booking booking);
}
