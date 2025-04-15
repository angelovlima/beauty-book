package br.com.beauty_book.professional_management.infra.gateway.integration.mapper;

import br.com.beauty_book.professional_management.domain.model.ServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferedDtoMapper {

    public ServiceOffered toDomain(ServiceOfferedDto dto) {
        return new ServiceOffered(
                dto.id(),
                dto.name()
        );
    }
}
