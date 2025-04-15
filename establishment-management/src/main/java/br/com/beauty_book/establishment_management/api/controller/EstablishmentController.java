package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.CreateEstablishmentApiRequest;
import br.com.beauty_book.establishment_management.api.dto.EstablishmentApiResponse;
import br.com.beauty_book.establishment_management.api.dto.OperatingHourApiResponse;
import br.com.beauty_book.establishment_management.api.dto.mapper.EstablishmentApiMapper;
import br.com.beauty_book.establishment_management.api.dto.mapper.OperatingHourApiMapper;
import br.com.beauty_book.establishment_management.domain.usecase.*;
import br.com.beauty_book.establishment_management.api.dto.UpdateEstablishmentApiRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establishments")
@RequiredArgsConstructor
public class EstablishmentController {

    private final CreateEstablishmentUseCase createUseCase;
    private final GetEstablishmentByIdUseCase getByIdUseCase;
    private final ListEstablishmentsUseCase listUseCase;
    private final DeleteEstablishmentUseCase deleteUseCase;
    private final UpdateEstablishmentUseCase updateUseCase;
    private final GetOperatingHoursByEstablishmentIdUseCase getOperatingHoursByEstablishmentIdUseCase;
    private final OperatingHourApiMapper operatingHourApiMapper;

    @Operation(
            summary = "Create Establishment",
            description = "Registers a new establishment with services, operating hours, and professionals.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Establishment successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EstablishmentApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<EstablishmentApiResponse> create(@Valid @RequestBody CreateEstablishmentApiRequest request) {
        var domain = EstablishmentApiMapper.toDomain(request);
        var created = createUseCase.execute(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(EstablishmentApiMapper.toResponse(created));
    }

    @Operation(
            summary = "Get Establishment by ID",
            description = "Retrieves an establishment by its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Establishment found",
                            content = @Content(schema = @Schema(implementation = EstablishmentApiResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Establishment not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentApiResponse> findById(@PathVariable Long id) {
        var found = getByIdUseCase.execute(id);
        return ResponseEntity.ok(EstablishmentApiMapper.toResponse(found));
    }

    @Operation(
            summary = "List Establishments",
            description = "Returns a list of all registered establishments.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of establishments",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EstablishmentApiResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<EstablishmentApiResponse>> listAll() {
        var list = listUseCase.execute();
        var response = list.stream()
                .map(EstablishmentApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Establishment",
            description = "Deletes an existing establishment by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Establishment successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Establishment not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update Establishment",
            description = "Updates an existing establishment's full data, including services, operating hours and professionals.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Establishment successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EstablishmentApiResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body"),
                    @ApiResponse(responseCode = "404", description = "Establishment not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EstablishmentApiResponse> update(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateEstablishmentApiRequest request) {
        var updatedDomain = EstablishmentApiMapper.toDomain(request);
        var updated = updateUseCase.execute(id, updatedDomain);
        return ResponseEntity.ok(EstablishmentApiMapper.toResponse(updated));
    }

    @GetMapping("/{id}/operating-hours")
    public ResponseEntity<List<OperatingHourApiResponse>> getOperatingHours(@PathVariable Long id) {
        var list = getOperatingHoursByEstablishmentIdUseCase.execute(id);
        return ResponseEntity.ok(
                list.stream()
                        .map(operatingHourApiMapper::toResponse)
                        .toList()
        );
    }

}
