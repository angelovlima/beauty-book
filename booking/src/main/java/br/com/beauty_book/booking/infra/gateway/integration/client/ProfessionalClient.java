package br.com.beauty_book.booking.infra.gateway.integration.client;

import br.com.beauty_book.booking.infra.gateway.integration.dto.AvailabilityDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "professional-management", path = "/professionals")
public interface ProfessionalClient {

    @GetMapping("/{id}")
    ProfessionalDto findById(@PathVariable("id") Long id);

    @GetMapping("/{id}/availabilities")
    List<AvailabilityDto> getAvailabilityByProfessionalId(@PathVariable("id") Long professionalId);
}
