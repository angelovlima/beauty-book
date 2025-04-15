package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Service offered by the establishment")
public record ServiceOfferedRequest(

        @NotBlank
        @Schema(description = "Service name", example = "Corte Feminino")
        String name,

        @Schema(description = "Optional description", example = "Corte com escova finalizadora")
        String description,

        @NotNull
        @Positive
        @Schema(description = "Price of the service", example = "89.90")
        Double price,

        @NotNull
        @Positive
        @Schema(description = "Duration in minutes", example = "45")
        Integer durationMinutes

) {}
