package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.CreateReviewApiRequest;
import br.com.beauty_book.establishment_management.api.dto.ReviewApiResponse;
import br.com.beauty_book.establishment_management.api.dto.mapper.ReviewApiMapper;
import br.com.beauty_book.establishment_management.domain.usecase.CreateReviewUseCase;
import br.com.beauty_book.establishment_management.domain.usecase.ListReviewsByEstablishmentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final CreateReviewUseCase createUseCase;
    private final ListReviewsByEstablishmentUseCase listUseCase;

    public ReviewController(CreateReviewUseCase createUseCase,
                            ListReviewsByEstablishmentUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
    }

    @Operation(
            summary = "Criar avaliação",
            description = "Cria uma nova avaliação para um estabelecimento por um cliente com agendamento COMPLETED.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateReviewApiRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de avaliação",
                                    value = """
                {
                    "customerId": 1,
                    "establishmentId": 3,
                    "stars": 5,
                    "comment": "Excelente atendimento!"
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Avaliação criada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "409", description = "Avaliação já existente")
            }
    )
    @PostMapping
    public ResponseEntity<ReviewApiResponse> create(@RequestBody @Valid CreateReviewApiRequest request) {
        var review = ReviewApiMapper.toDomain(request);
        var saved = createUseCase.execute(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewApiMapper.toResponse(saved));
    }

    @Operation(
            summary = "Listar avaliações por estabelecimento",
            description = "Retorna todas as avaliações associadas a um estabelecimento.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de avaliações",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ReviewApiResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/establishment/{establishmentId}")
    public ResponseEntity<List<ReviewApiResponse>> findAllByEstablishment(@PathVariable Long establishmentId) {
        var list = listUseCase.execute(establishmentId);
        var response = list.stream().map(ReviewApiMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
}
