package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Reference to a professional by ID")
public record ProfessionalIdRequest(

        @NotNull
        @Schema(description = "ID of the professional", example = "5")
        Long professionalId

) {}
