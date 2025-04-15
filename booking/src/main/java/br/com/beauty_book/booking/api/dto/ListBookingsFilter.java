package br.com.beauty_book.booking.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ListBookingsFilter(

        @Schema(description = "Customer ID", example = "1")
        @Parameter(description = "Filter by customer ID", example = "1")
        Long customerId,

        @Schema(description = "Professional ID", example = "2")
        @Parameter(description = "Filter by professional ID", example = "2")
        Long professionalId,

        @Schema(description = "Establishment ID", example = "3")
        @Parameter(description = "Filter by establishment ID", example = "3")
        Long establishmentId,

        @Schema(description = "Date of the booking", example = "2025-04-20")
        @Parameter(description = "Filter by booking date", example = "2025-04-20")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate bookingDate
) {}
