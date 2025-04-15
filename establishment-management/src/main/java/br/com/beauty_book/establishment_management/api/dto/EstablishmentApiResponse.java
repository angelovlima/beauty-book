package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing a registered establishment")
public record EstablishmentApiResponse(

        @Schema(description = "Establishment ID", example = "1")
        Long id,

        @Schema(description = "Name of the establishment", example = "Studio Beleza Pura")
        String name,

        @Schema(description = "Complete address", example = "Av. Paulista, 1000, São Paulo - SP")
        String address,

        @Schema(description = "Photo URL", example = "https://example.com/images/salao.jpg")
        String photoUrl

) {}
