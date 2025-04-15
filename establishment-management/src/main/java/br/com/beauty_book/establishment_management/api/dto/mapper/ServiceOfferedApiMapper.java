package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.ServiceOfferedApiResponse;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferedApiMapper {

    public ServiceOfferedApiResponse toResponse(ServiceOffered domain) {
        return new ServiceOfferedApiResponse(
                domain.id(),
                domain.name(),
                domain.description(),
                domain.price(),
                domain.durationMinutes()
        );
    }
}
