package br.com.beauty_book.professional_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a weekly availability slot")
public record AvailabilityRequest(

        @Schema(description = "Day of week (0=Sunday, 6=Saturday)", example = "1")
        Integer dayOfWeek,

        @Schema(description = "Start time (HH:mm)", example = "09:00")
        String startTime,

        @Schema(description = "End time (HH:mm)", example = "17:00")
        String endTime
) {}
