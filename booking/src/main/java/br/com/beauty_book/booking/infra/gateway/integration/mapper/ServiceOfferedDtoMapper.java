package br.com.beauty_book.booking.infra.gateway.integration.mapper;

import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferedDtoMapper {

    public Integer extractDurationMinutes(ServiceOfferedDto dto) {
        return dto.durationMinutes();
    }
}
