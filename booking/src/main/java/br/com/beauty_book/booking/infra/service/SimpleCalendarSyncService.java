package br.com.beauty_book.booking.infra.service;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.service.CalendarSyncService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SimpleCalendarSyncService implements CalendarSyncService {

    @Override
    public String generateICalendarContent(Booking booking) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        LocalDateTime start = booking.getBookingDate().atTime(booking.getStartTime());
        LocalDateTime end = booking.getBookingDate().atTime(booking.getEndTime());

        return """
            BEGIN:VEVENT
            UID:booking-%s@beautybook.com
            DTSTAMP:%s
            DTSTART:%s
            DTEND:%s
            SUMMARY:Agendamento - Servico %s
            DESCRIPTION:Agendamento com profissional ID %s via BeautyBook
            LOCATION:Estabelecimento ID %s
            END:VEVENT
            """.formatted(
                booking.getId(),
                formatter.format(start),
                formatter.format(start),
                formatter.format(end),
                booking.getServiceId(),
                booking.getProfessionalId(),
                booking.getEstablishmentId()
        );
    }

}
