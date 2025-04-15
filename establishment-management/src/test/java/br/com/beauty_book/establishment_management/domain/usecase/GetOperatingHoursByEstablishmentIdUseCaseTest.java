package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.OperatingHourGateway;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetOperatingHoursByEstablishmentIdUseCaseTest {

    private OperatingHourGateway gateway;
    private GetOperatingHoursByEstablishmentIdUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(OperatingHourGateway.class);
        useCase = new GetOperatingHoursByEstablishmentIdUseCase(gateway);
    }

    @Test
    void shouldReturnOperatingHoursForGivenEstablishmentId() {
        Long establishmentId = 1L;
        List<OperatingHour> expected = List.of(
                new OperatingHour(1L, 1, LocalTime.of(9, 0), LocalTime.of(18, 0)),
                new OperatingHour(2L, 2, LocalTime.of(10, 0), LocalTime.of(17, 0))
        );

        when(gateway.findByEstablishmentId(establishmentId)).thenReturn(expected);

        List<OperatingHour> result = useCase.execute(establishmentId);

        assertThat(result).hasSize(2).containsExactlyElementsOf(expected);
        verify(gateway).findByEstablishmentId(establishmentId);
    }
}
