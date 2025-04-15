package br.com.beauty_book.booking.api.controller;

import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.domain.service.CalendarSyncService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings/calendar-download")
public class CalendarDownloadController {

    private final BookingGateway gateway;
    private final CalendarSyncService calendarSyncService;

    public CalendarDownloadController(BookingGateway gateway, CalendarSyncService calendarSyncService) {
        this.gateway = gateway;
        this.calendarSyncService = calendarSyncService;
    }

    @GetMapping("/customer/{customerId}")
    public void downloadCalendarForCustomer(@PathVariable Long customerId, HttpServletResponse response) throws IOException {
        List<Booking> bookings = gateway.findAllWithFilters(customerId, null, null, null).stream()
                .filter(b -> b.getStatus() == BookingStatus.SCHEDULED || b.getStatus() == BookingStatus.RESCHEDULED)
                .filter(b -> !b.getBookingDate().isBefore(LocalDate.now()))
                .toList();

        generateAndWriteIcs("agenda-customer-" + customerId + ".ics", bookings, response);
    }

    @GetMapping("/professional/{professionalId}")
    public void downloadCalendarForProfessional(@PathVariable Long professionalId, HttpServletResponse response) throws IOException {
        List<Booking> bookings = gateway.findAllWithFilters(null, professionalId, null, null).stream()
                .filter(b -> b.getStatus() == BookingStatus.SCHEDULED || b.getStatus() == BookingStatus.RESCHEDULED)
                .filter(b -> !b.getBookingDate().isBefore(LocalDate.now()))
                .toList();

        generateAndWriteIcs("agenda-professional-" + professionalId + ".ics", bookings, response);
    }

    private void generateAndWriteIcs(String filename, List<Booking> bookings, HttpServletResponse response) throws IOException {
        StringBuilder icsContent = new StringBuilder("""
                BEGIN:VCALENDAR
                VERSION:2.0
                PRODID:-//BeautyBook//Booking System//EN
                """);

        for (Booking booking : bookings) {
            icsContent.append(calendarSyncService.generateICalendarContent(booking)).append("\n");
        }

        icsContent.append("END:VCALENDAR\n");

        response.setContentType("text/calendar");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(icsContent.toString());
        }
    }
}