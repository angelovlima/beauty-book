package br.com.beauty_book.booking.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload to create a booking between customer and professional")
public record CreateBookingApiRequest(

        @NotNull
        @Schema(description = "Customer ID", example = "1")
        Long customerId,

        @NotNull
        @Schema(description = "Professional ID", example = "2")
        Long professionalId,

        @NotNull
        @Schema(description = "Establishment ID", example = "3")
        Long establishmentId,

        @NotNull
        @Schema(description = "Service ID", example = "4")
        Long serviceId,

        @NotNull
        @Schema(description = "Date of the booking", example = "2025-04-20")
        LocalDate bookingDate,

        @NotNull
        @Schema(description = "Start time of the booking", example = "10:00", format = "HH:mm")
        LocalTime startTime
) {}