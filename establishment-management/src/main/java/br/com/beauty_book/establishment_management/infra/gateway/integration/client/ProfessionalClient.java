package br.com.beauty_book.establishment_management.infra.gateway.integration.client;

import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.ProfessionalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "professional-management",
        path = "/professionals"
)
public interface ProfessionalClient {

    @GetMapping("/{id}")
    ProfessionalDto findById(@PathVariable("id") Long id);
}
