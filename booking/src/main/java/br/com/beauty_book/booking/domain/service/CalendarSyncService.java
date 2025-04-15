package br.com.beauty_book.booking.domain.service;

import br.com.beauty_book.booking.domain.model.Booking;

public interface CalendarSyncService {
    String generateICalendarContent(Booking booking);
}
