package br.com.beauty_book.professional_management.infra.gateway.integration.client;

import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "establishment-management",
        path = "/services"
)
public interface ServiceOfferedClient {

    @GetMapping("/{id}")
    ServiceOfferedDto findById(@PathVariable("id") Long id);
}