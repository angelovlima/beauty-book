package br.com.beauty_book.professional_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ID of a service offered linked to the professional")
public record ServiceOfferedIdRequest(

        @Schema(description = "Service ID", example = "5")
        Long id
) {}
