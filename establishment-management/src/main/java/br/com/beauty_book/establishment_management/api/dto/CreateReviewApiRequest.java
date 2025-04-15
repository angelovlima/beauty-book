package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload para criar avaliação de um estabelecimento")
public record CreateReviewApiRequest(
        @Schema(description = "ID do cliente", example = "1")
        Long customerId,

        @Schema(description = "ID do estabelecimento", example = "3")
        Long establishmentId,

        @Schema(description = "Nota (1 a 5)", example = "5")
        Integer stars,

        @Schema(description = "Comentário da avaliação", example = "Ótimo atendimento, recomendo!")
        String comment
) {}
