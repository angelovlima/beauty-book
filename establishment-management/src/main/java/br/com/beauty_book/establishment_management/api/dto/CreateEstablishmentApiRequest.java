package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request payload for creating an establishment with all relevant data")
public record CreateEstablishmentApiRequest(

        @NotBlank
        @Schema(description = "Name of the establishment", example = "Studio Beleza Pura")
        String name,

        @NotBlank
        @Schema(description = "Complete address", example = "Av. Paulista, 1000, São Paulo - SP")
        String address,

        @Schema(description = "Photo URL", example = "https://example.com/images/salao.jpg")
        String photoUrl,

        @NotNull
        @Schema(description = "Operating hours for each day of the week")
        List<OperatingHourRequest> operatingHours,

        @NotNull
        @Schema(description = "List of services offered")
        List<ServiceOfferedRequest> services,

        @NotNull
        @Schema(description = "List of professionals associated (by ID)")
        List<ProfessionalIdRequest> professionals

) {}
