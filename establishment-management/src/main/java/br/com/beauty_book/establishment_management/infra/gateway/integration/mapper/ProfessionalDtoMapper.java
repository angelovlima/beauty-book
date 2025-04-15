package br.com.beauty_book.establishment_management.infra.gateway.integration.mapper;

import br.com.beauty_book.establishment_management.domain.model.Professional;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.ProfessionalDto;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalDtoMapper {

    public Professional toDomain(ProfessionalDto dto) {
        return new Professional(
                dto.id(),
                dto.name()
        );
    }
}
