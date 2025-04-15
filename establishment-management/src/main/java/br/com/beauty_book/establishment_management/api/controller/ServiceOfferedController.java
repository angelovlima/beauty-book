package br.com.beauty_book.establishment_management.api.controller;

import br.com.beauty_book.establishment_management.api.dto.ServiceOfferedApiResponse;
import br.com.beauty_book.establishment_management.api.dto.mapper.ServiceOfferedApiMapper;
import br.com.beauty_book.establishment_management.domain.usecase.GetServiceOfferedByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class ServiceOfferedController {

    private final GetServiceOfferedByIdUseCase getByIdUseCase;
    private final ServiceOfferedApiMapper mapper;

    public ServiceOfferedController(GetServiceOfferedByIdUseCase getByIdUseCase,
                                    ServiceOfferedApiMapper mapper) {
        this.getByIdUseCase = getByIdUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferedApiResponse> findById(@PathVariable Long id) {
        var service = getByIdUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(service));
    }
}
