package br.com.beauty_book.booking.infra.service;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleCalendarSyncServiceTest {

    private SimpleCalendarSyncService service;

    @BeforeEach
    void setUp() {
        service = new SimpleCalendarSyncService();
    }

    @Test
    void shouldGenerateICalendarContent() {
        Booking booking = new Booking(
                1L,
                101L,
                202L,
                303L,
                404L,
                LocalDate.of(2025, 4, 15),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                BookingStatus.SCHEDULED
        );

        String icsContent = service.generateICalendarContent(booking);

        assertThat(icsContent).contains("BEGIN:VEVENT");
        assertThat(icsContent).contains("UID:booking-1@beautybook.com");
        assertThat(icsContent).contains("DTSTART:20250415T100000Z");
        assertThat(icsContent).contains("DTEND:20250415T110000Z");
        assertThat(icsContent).contains("SUMMARY:Agendamento - Servico 404");
        assertThat(icsContent).contains("DESCRIPTION:Agendamento com profissional ID 202 via BeautyBook");
        assertThat(icsContent).contains("LOCATION:Estabelecimento ID 303");
        assertThat(icsContent).contains("END:VEVENT");
    }
}
