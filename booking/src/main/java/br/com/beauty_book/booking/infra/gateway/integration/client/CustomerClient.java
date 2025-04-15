package br.com.beauty_book.booking.infra.gateway.integration.client;

import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-management", path = "/customers")
public interface CustomerClient {

    @GetMapping("/{id}")
    CustomerDto findById(@PathVariable("id") Long id);
}
