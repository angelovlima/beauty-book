package br.com.beauty_book.professional_management.api.controller;

import br.com.beauty_book.professional_management.api.dto.*;
import br.com.beauty_book.professional_management.api.dto.mapper.AvailabilityApiMapper;
import br.com.beauty_book.professional_management.api.dto.mapper.ProfessionalApiMapper;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/professionals")
public class ProfessionalController {

    private final CreateProfessionalUseCase createUseCase;
    private final UpdateProfessionalUseCase updateUseCase;
    private final DeleteProfessionalUseCase deleteUseCase;
    private final FindProfessionalByIdUseCase findByIdUseCase;
    private final FindProfessionalByCpfUseCase findByCpfUseCase;
    private final ListAllProfessionalsUseCase listAllUseCase;
    private final FindAvailabilitiesByProfessionalIdUseCase findAvailabilitiesByProfessionalIdUseCase;
    private final AvailabilityApiMapper availabilityApiMapper;

    @Operation(
            summary = "Register new professional",
            description = "Creates a professional and registers their availability and services.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Professional created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "CPF already registered")
            }
    )
    @PostMapping
    public ResponseEntity<ProfessionalApiResponse> create(@RequestBody CreateProfessionalApiRequest request) {
        var domain = ProfessionalApiMapper.toDomain(request);
        var created = createUseCase.execute(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfessionalApiMapper.toResponse(created));
    }

    @Operation(
            summary = "List all professionals",
            description = "Returns a list of all professionals in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProfessionalApiResponse>> listAll() {
        var professionals = listAllUseCase.execute();
        var response = professionals.stream()
                .map(ProfessionalApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get professional by ID",
            description = "Retrieves a single professional by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professional found"),
                    @ApiResponse(responseCode = "404", description = "Professional not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalApiResponse> findById(@PathVariable Long id) {
        var professional = findByIdUseCase.execute(id);
        return ResponseEntity.ok(ProfessionalApiMapper.toResponse(professional));
    }

    @Operation(
            summary = "Get professional by CPF",
            description = "Retrieves a single professional by their CPF.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professional found"),
                    @ApiResponse(responseCode = "404", description = "Professional not found")
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ProfessionalApiResponse> findByCpf(@PathVariable String cpf) {
        var professional = findByCpfUseCase.execute(new Cpf(cpf));
        return ResponseEntity.ok(ProfessionalApiMapper.toResponse(professional));
    }

    @Operation(
            summary = "Update professional",
            description = "Updates an existing professional's information.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Professional updated"),
                    @ApiResponse(responseCode = "404", description = "Professional not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalApiResponse> update(@PathVariable Long id,
                                                          @RequestBody UpdateProfessionalApiRequest request) {
        var domain = ProfessionalApiMapper.toDomain(request, id);
        var updated = updateUseCase.execute(domain);
        return ResponseEntity.ok(ProfessionalApiMapper.toResponse(updated));
    }

    @Operation(
            summary = "Delete professional",
            description = "Deletes a professional by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Professional deleted"),
                    @ApiResponse(responseCode = "404", description = "Professional not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get availability for professional",
            description = "Returns a list of available times for a given professional",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Availability list returned"),
                    @ApiResponse(responseCode = "404", description = "Professional not found")
            }
    )
    @GetMapping("/{id}/availabilities")
    public ResponseEntity<List<AvailabilityApiResponse>> findAvailabilities(@PathVariable Long id) {
        var list = findAvailabilitiesByProfessionalIdUseCase.execute(id);
        var response = list.stream()
                .map(availabilityApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

}
