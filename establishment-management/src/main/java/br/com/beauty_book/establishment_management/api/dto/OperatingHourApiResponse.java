package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "Operating hour for a specific day of the week")
public record OperatingHourApiResponse(
        @Schema(description = "Day of week (0=Sunday to 6=Saturday)", example = "1")
        Integer dayOfWeek,

        @Schema(description = "Opening time", example = "09:00")
        LocalTime startTime,

        @Schema(description = "Closing time", example = "18:00")
        LocalTime endTime
) {}
