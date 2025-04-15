package br.com.beauty_book.professional_management.api.dto.mapper;

import br.com.beauty_book.professional_management.api.dto.AvailabilityApiResponse;
import br.com.beauty_book.professional_management.domain.model.Availability;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvailabilityApiMapperTest {

    private final AvailabilityApiMapper mapper = new AvailabilityApiMapper();

    @Test
    void shouldMapDomainToApiResponseCorrectly() {
        var domain = new Availability(
                1L,
                2, // Terça-feira
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );

        AvailabilityApiResponse response = mapper.toResponse(domain);

        assertThat(response).isNotNull();
        assertThat(response.dayOfWeek()).isEqualTo(2);
        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(17, 0));
    }
}
