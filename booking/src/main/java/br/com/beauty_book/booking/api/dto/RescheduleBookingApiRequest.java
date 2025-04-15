package br.com.beauty_book.booking.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload for rescheduling a booking")
public record RescheduleBookingApiRequest(

        @Schema(description = "New booking date", example = "2025-04-25")
        LocalDate newBookingDate,

        @Schema(description = "New start time", example = "15:00", format = "HH:mm")
        LocalTime newStartTime
) {}
