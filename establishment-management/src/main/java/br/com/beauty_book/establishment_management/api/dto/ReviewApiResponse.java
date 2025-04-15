package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta representando uma avaliação")
public record ReviewApiResponse(
        @Schema(description = "ID da avaliação", example = "10")
        Long id,

        @Schema(description = "ID do cliente", example = "1")
        Long customerId,

        @Schema(description = "ID do estabelecimento", example = "3")
        Long establishmentId,

        @Schema(description = "Nota da avaliação", example = "5")
        Integer stars,

        @Schema(description = "Comentário", example = "Ótimo atendimento")
        String comment
) {}
