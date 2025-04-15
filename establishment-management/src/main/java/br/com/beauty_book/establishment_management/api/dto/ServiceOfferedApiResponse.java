package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Service offered details for external validation")
public record ServiceOfferedApiResponse(

        @Schema(description = "ID of the service", example = "5")
        Long id,

        @Schema(description = "Service name", example = "Corte Feminino")
        String name,

        @Schema(description = "Description of the service", example = "Corte de cabelo feminino com finalização")
        String description,

        @Schema(description = "Price of the service", example = "49.90")
        BigDecimal price,

        @Schema(description = "Duration in minutes", example = "45")
        Integer durationMinutes
) {}
