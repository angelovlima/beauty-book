package br.com.beauty_book.professional_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "Availability data for a professional")
public record AvailabilityApiResponse(

        @Schema(description = "Day of the week (0=Sunday to 6=Saturday)", example = "1")
        Integer dayOfWeek,

        @Schema(description = "Start time", example = "09:00")
        LocalTime startTime,

        @Schema(description = "End time", example = "18:00")
        LocalTime endTime
) {}
