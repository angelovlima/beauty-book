package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.model.Availability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FindAvailabilitiesByProfessionalIdUseCaseTest {

    private AvailabilityGateway gateway;
    private FindAvailabilitiesByProfessionalIdUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(AvailabilityGateway.class);
        useCase = new FindAvailabilitiesByProfessionalIdUseCase(gateway);
    }

    @Test
    void shouldReturnAvailabilitiesForProfessional() {
        Long professionalId = 1L;
        List<Availability> mockAvailabilities = List.of(
                new Availability(10L, 1, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                new Availability(11L, 3, LocalTime.of(14, 0), LocalTime.of(18, 0))
        );

        when(gateway.findAllByProfessionalId(professionalId)).thenReturn(mockAvailabilities);

        List<Availability> result = useCase.execute(professionalId);

        assertThat(result).isEqualTo(mockAvailabilities);
        verify(gateway).findAllByProfessionalId(professionalId);
    }
}
