package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.OperatingHourApiResponse;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class OperatingHourApiMapperTest {

    private OperatingHourApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OperatingHourApiMapper();
    }

    @Test
    void shouldMapDomainToResponse() {
        OperatingHour domain = new OperatingHour(1L, 1, LocalTime.of(9, 0), LocalTime.of(18, 0));

        OperatingHourApiResponse response = mapper.toResponse(domain);

        assertThat(response.dayOfWeek()).isEqualTo(1);
        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(18, 0));
    }
}
