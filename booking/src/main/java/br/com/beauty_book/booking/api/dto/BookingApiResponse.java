package br.com.beauty_book.booking.api.dto;

import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingApiResponse(
        Long id,
        Long customerId,
        Long professionalId,
        Long establishmentId,
        Long serviceId,
        LocalDate bookingDate,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,
        BookingStatus status
) {}
