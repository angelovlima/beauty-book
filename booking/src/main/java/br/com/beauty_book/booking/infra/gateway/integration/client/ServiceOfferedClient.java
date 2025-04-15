package br.com.beauty_book.booking.infra.gateway.integration.client;

import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "establishment-management",
        contextId = "serviceOfferedClient",
        path = "/services"
)
public interface ServiceOfferedClient {

    @GetMapping("/{id}")
    ServiceOfferedDto findById(@PathVariable("id") Long id);
}
