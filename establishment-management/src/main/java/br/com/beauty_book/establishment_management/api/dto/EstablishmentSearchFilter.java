package br.com.beauty_book.establishment_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Parâmetros de filtro para busca de estabelecimentos")
public record EstablishmentSearchFilter(

        @Schema(description = "Parte do nome do estabelecimento", example = "Studio Beleza")
        String name,

        @Schema(description = "Parte do endereço ou cidade", example = "Paulista")
        String location,

        @Schema(description = "Parte do nome do serviço oferecido", example = "Corte")
        String serviceName,

        @Schema(description = "Nota mínima de avaliação (1 a 5)", example = "4")
        Integer minRating,

        @Schema(description = "Dia da semana de funcionamento (0=Domingo, 6=Sábado)", example = "1")
        Integer dayOfWeekAvailable,

        @Schema(description = "Preço mínimo do serviço", example = "30.00")
        BigDecimal minPrice,

        @Schema(description = "Preço máximo do serviço", example = "80.00")
        BigDecimal maxPrice

) {}
