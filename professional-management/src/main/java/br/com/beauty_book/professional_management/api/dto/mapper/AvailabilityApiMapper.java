package br.com.beauty_book.professional_management.api.dto.mapper;

import br.com.beauty_book.professional_management.api.dto.AvailabilityApiResponse;
import br.com.beauty_book.professional_management.domain.model.Availability;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityApiMapper {

    public AvailabilityApiResponse toResponse(Availability availability) {
        return new AvailabilityApiResponse(
                availability.dayOfWeek(),
                availability.startTime(),
                availability.endTime()
        );
    }
}
