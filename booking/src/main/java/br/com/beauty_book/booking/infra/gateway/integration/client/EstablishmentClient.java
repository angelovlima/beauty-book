package br.com.beauty_book.booking.infra.gateway.integration.client;

import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.OperatingHourDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "establishment-management",
        contextId = "establishmentClient",
        path = "/establishments"
)
public interface EstablishmentClient {

    @GetMapping("/{id}")
    EstablishmentDto findById(@PathVariable("id") Long id);

    @GetMapping("/{id}/operating-hours")
    List<OperatingHourDto> getOperatingHoursByEstablishmentId(@PathVariable("id") Long id);
}
