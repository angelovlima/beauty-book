package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "Request payload for updating an establishment")
public record UpdateEstablishmentApiRequest(

        @NotBlank
        @Schema(description = "Updated name of the establishment", example = "Studio Beleza Pura Novo")
        String name,

        @NotBlank
        @Schema(description = "Updated address", example = "Av. Brasil, 1500, São Paulo - SP")
        String address,

        @Schema(description = "New photo URL", example = "https://example.com/images/novo.jpg")
        String photoUrl,

        @NotNull
        @Schema(description = "Updated operating hours")
        List<OperatingHourRequest> operatingHours,

        @NotNull
        @Schema(description = "Updated list of services offered")
        List<ServiceOfferedRequest> services,

        @NotNull
        @Schema(description = "Updated list of professionals (by ID)")
        List<ProfessionalIdRequest> professionals

) {}
