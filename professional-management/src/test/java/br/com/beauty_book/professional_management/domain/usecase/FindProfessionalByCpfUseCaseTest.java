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

class FindProfessionalByCpfUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private AvailabilityGateway availabilityGateway;
    private ProfessionalServiceOfferedGateway serviceOfferedGateway;
    private FindProfessionalByCpfUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        availabilityGateway = mock(AvailabilityGateway.class);
        serviceOfferedGateway = mock(ProfessionalServiceOfferedGateway.class);
        useCase = new FindProfessionalByCpfUseCase(professionalGateway, availabilityGateway, serviceOfferedGateway);
    }

    @Test
    void shouldReturnProfessionalWithAvailabilityAndServices() {
        var cpf = new Cpf("123.456.789-00");
        var basic = new Professional(
                1L,
                "Ana",
                cpf,
                "+55 11 91234-5678",
                new Email("ana@example.com"),
                null,
                null
        );

        var availabilityList = List.of(new Availability(1L, 1, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        var serviceList = List.of(new ProfessionalServiceOffered(1L, 100L));

        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(basic));
        when(availabilityGateway.findAllByProfessionalId(1L)).thenReturn(availabilityList);
        when(serviceOfferedGateway.findAllByProfessionalId(1L)).thenReturn(serviceList);

        var result = useCase.execute(cpf);

        assertThat(result).isNotNull();
        assertThat(result.availabilityList()).isEqualTo(availabilityList);
        assertThat(result.services()).isEqualTo(serviceList);
    }

    @Test
    void shouldThrowWhenProfessionalNotFound() {
        var cpf = new Cpf("000.000.000-00");
        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(cpf))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessageContaining(cpf.getValue());

        verify(availabilityGateway, never()).findAllByProfessionalId(any());
        verify(serviceOfferedGateway, never()).findAllByProfessionalId(any());
    }
}
