package br.com.beauty_book.professional_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Professional data returned in responses")
public record ProfessionalApiResponse(

        @Schema(description = "Unique identifier of the professional", example = "1")
        Long id,

        @Schema(description = "Professional full name", example = "Ana Costa")
        String name,

        @Schema(description = "CPF of the professional", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Phone number with area code", example = "+55 11 91234-5678")
        String phoneNumber,

        @Schema(description = "Email address", example = "ana@example.com")
        String email
) {}
