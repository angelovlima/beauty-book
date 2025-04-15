package br.com.beauty_book.professional_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Payload to register a new professional")
public record CreateProfessionalApiRequest(

        @NotBlank
        @Schema(description = "Professional full name", example = "Ana Costa")
        String name,

        @NotBlank
        @Schema(description = "CPF of the professional", example = "123.456.789-00")
        String cpf,

        @NotBlank
        @Schema(description = "Phone number with area code", example = "55 11 91234-5678")
        String phoneNumber,

        @Schema(description = "Email address", example = "ana@example.com")
        String email,

        @NotNull
        @Schema(description = "Weekly availability list of the professional")
        List<AvailabilityRequest> availabilityList,

        @NotNull
        @Schema(description = "List of service_offered IDs this professional executes")
        List<ServiceOfferedIdRequest> services
) {}
