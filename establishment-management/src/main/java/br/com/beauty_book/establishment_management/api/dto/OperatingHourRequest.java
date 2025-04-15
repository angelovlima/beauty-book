package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Defines a day's availability for the establishment")
public record OperatingHourRequest(

        @Min(0)
        @Max(6)
        @Schema(description = "Day of week (0=Sunday to 6=Saturday)", example = "1")
        int dayOfWeek,

        @NotBlank
        @Schema(description = "Opening time", example = "09:00")
        String startTime,

        @NotBlank
        @Schema(description = "Closing time", example = "18:00")
        String endTime

) {}
