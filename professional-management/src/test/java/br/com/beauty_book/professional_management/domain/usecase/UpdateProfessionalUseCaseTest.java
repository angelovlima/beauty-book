package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalAlreadyExistsException;
import br.com.beauty_book.professional_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.gateway.ServiceOfferedGateway;
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

class UpdateProfessionalUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private AvailabilityGateway availabilityGateway;
    private ProfessionalServiceOfferedGateway serviceGateway;
    private ServiceOfferedGateway serviceOfferedGateway;
    private UpdateProfessionalUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        availabilityGateway = mock(AvailabilityGateway.class);
        serviceGateway = mock(ProfessionalServiceOfferedGateway.class);
        serviceOfferedGateway = mock(ServiceOfferedGateway.class);
        useCase = new UpdateProfessionalUseCase(professionalGateway, availabilityGateway, serviceGateway, serviceOfferedGateway);
    }

    @Test
    void shouldUpdateProfessionalSuccessfully() {
        var cpf = new Cpf("123.456.789-00");
        var professional = new Professional(
                1L,
                "Bruna",
                cpf,
                "+55 11 98888-1111",
                new Email("bruna@example.com"),
                List.of(new Availability(null, 1, LocalTime.of(9, 0), LocalTime.of(12, 0))),
                List.of(new ProfessionalServiceOffered(null, 10L))
        );

        when(professionalGateway.findByCpf(cpf.getValue()))
                .thenReturn(Optional.of(professional));
        when(serviceOfferedGateway.findById(10L))
                .thenReturn(Optional.of(mock()));
        when(professionalGateway.update(professional))
                .thenReturn(professional);

        var result = useCase.execute(professional);

        assertThat(result).isEqualTo(professional);

        verify(availabilityGateway).deleteAllByProfessionalId(1L);
        verify(serviceGateway).deleteAllByProfessionalId(1L);
        verify(availabilityGateway).saveAll(1L, professional.availabilityList());
        verify(serviceGateway).saveAll(1L, professional.services());
    }

    @Test
    void shouldThrowWhenCpfIsUsedByAnotherProfessional() {
        var cpf = new Cpf("123.456.789-00");
        var existing = new Professional(2L, "Outro", cpf, "x", new Email("x@x.com"), null, null);
        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(existing));

        var update = new Professional(
                1L,
                "Bruna",
                cpf,
                "+55 11 98888-1111",
                new Email("bruna@example.com"),
                List.of(),
                List.of()
        );

        assertThatThrownBy(() -> useCase.execute(update))
                .isInstanceOf(ProfessionalAlreadyExistsException.class)
                .hasMessageContaining(cpf.getValue());

        verify(professionalGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenServiceNotFound() {
        var cpf = new Cpf("123.456.789-00");
        var update = new Professional(
                1L,
                "Bruna",
                cpf,
                "+55 11 98888-1111",
                new Email("bruna@example.com"),
                List.of(),
                List.of(new ProfessionalServiceOffered(null, 99L))
        );

        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.of(update));
        when(serviceOfferedGateway.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(update))
                .isInstanceOf(ServiceOfferedNotFoundException.class)
                .hasMessageContaining("99");

        verify(professionalGateway, never()).update(any());
    }
}
