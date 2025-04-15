package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.api.dto.EstablishmentApiResponse;
import br.com.beauty_book.establishment_management.api.dto.mapper.EstablishmentApiMapper;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.domain.usecase.SearchEstablishmentsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establishments/search")
public class EstablishmentSearchController {

    private final SearchEstablishmentsUseCase searchUseCase;

    public EstablishmentSearchController(SearchEstablishmentsUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    @Operation(
            summary = "Buscar e filtrar estabelecimentos",
            description = "Filtra estabelecimentos por nome, localização, serviço oferecido, avaliação mínima, dia da semana disponível e faixa de preço.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de estabelecimentos filtrados",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = EstablishmentApiResponse.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<EstablishmentApiResponse>> search(EstablishmentSearchFilter filter) {
        List<Establishment> results = searchUseCase.execute(filter);
        List<EstablishmentApiResponse> response = results.stream()
                .map(EstablishmentApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
