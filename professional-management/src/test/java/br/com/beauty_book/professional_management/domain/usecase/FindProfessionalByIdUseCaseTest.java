package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.Availability;
import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.ProfessionalServiceOffered;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindProfessionalByIdUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private AvailabilityGateway availabilityGateway;
    private ProfessionalServiceOfferedGateway serviceOfferedGateway;
    private FindProfessionalByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        availabilityGateway = mock(AvailabilityGateway.class);
        serviceOfferedGateway = mock(ProfessionalServiceOfferedGateway.class);
        useCase = new FindProfessionalByIdUseCase(professionalGateway, availabilityGateway, serviceOfferedGateway);
    }

    @Test
    void shouldReturnProfessionalWithAvailabilityAndServices() {
        var id = 1L;
        var basic = new Professional(
                id,
                "Carla",
                new Cpf("123.456.789-00"),
                "+55 11 98888-0000",
                new Email("carla@example.com"),
                null,
                null
        );

        var availabilityList = List.of(new Availability(1L, 1, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        var serviceList = List.of(new ProfessionalServiceOffered(1L, 10L));

        when(professionalGateway.findById(id)).thenReturn(Optional.of(basic));
        when(availabilityGateway.findAllByProfessionalId(id)).thenReturn(availabilityList);
        when(serviceOfferedGateway.findAllByProfessionalId(id)).thenReturn(serviceList);

        var result = useCase.execute(id);

        assertThat(result).isNotNull();
        assertThat(result.availabilityList()).isEqualTo(availabilityList);
        assertThat(result.services()).isEqualTo(serviceList);
    }

    @Test
    void shouldThrowWhenProfessionalNotFound() {
        Long id = 999L;
        when(professionalGateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

        verify(availabilityGateway, never()).findAllByProfessionalId(any());
        verify(serviceOfferedGateway, never()).findAllByProfessionalId(any());
    }
}
