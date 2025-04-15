package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.OperatingHourApiResponse;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import org.springframework.stereotype.Component;

@Component
public class OperatingHourApiMapper {

    public OperatingHourApiResponse toResponse(OperatingHour domain) {
        return new OperatingHourApiResponse(
                domain.dayOfWeek(),
                domain.startTime(),
                domain.endTime()
        );
    }
}
