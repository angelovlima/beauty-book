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

class CreateProfessionalUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private AvailabilityGateway availabilityGateway;
    private ProfessionalServiceOfferedGateway serviceGateway;
    private ServiceOfferedGateway serviceOfferedGateway;
    private CreateProfessionalUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        availabilityGateway = mock(AvailabilityGateway.class);
        serviceGateway = mock(ProfessionalServiceOfferedGateway.class);
        serviceOfferedGateway = mock(ServiceOfferedGateway.class);
        useCase = new CreateProfessionalUseCase(professionalGateway, availabilityGateway, serviceGateway, serviceOfferedGateway);
    }

    @Test
    void shouldCreateProfessionalSuccessfully() {
        var cpf = new Cpf("123.456.789-00");
        var email = new Email("ana@example.com");

        var professional = new Professional(
                null,
                "Ana Costa",
                cpf,
                "+55 11 91234-5678",
                email,
                List.of(new Availability(null, 1, LocalTime.of(9, 0), LocalTime.of(17, 0))),
                List.of(new ProfessionalServiceOffered(null, 1L))
        );

        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.empty());
        when(serviceOfferedGateway.findById(1L)).thenReturn(Optional.of(mock()));
        when(professionalGateway.save(professional)).thenReturn(professional);

        var result = useCase.execute(professional);

        assertThat(result).isNotNull();
        verify(professionalGateway).save(professional);
        verify(availabilityGateway).saveAll(any(), eq(professional.availabilityList()));
        verify(serviceGateway).saveAll(any(), eq(professional.services()));
    }

    @Test
    void shouldThrowWhenCpfAlreadyExists() {
        var cpf = new Cpf("123.456.789-00");
        when(professionalGateway.findByCpf(cpf.getValue()))
                .thenReturn(Optional.of(mock(Professional.class)));

        var professional = new Professional(
                null,
                "Ana Costa",
                cpf,
                "+55 11 91234-5678",
                new Email("ana@example.com"),
                List.of(),
                List.of()
        );

        assertThatThrownBy(() -> useCase.execute(professional))
                .isInstanceOf(ProfessionalAlreadyExistsException.class)
                .hasMessageContaining(cpf.getValue());

        verify(professionalGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenServiceNotFound() {
        var cpf = new Cpf("123.456.789-00");
        var email = new Email("ana@example.com");

        var professional = new Professional(
                null,
                "Ana Costa",
                cpf,
                "+55 11 91234-5678",
                email,
                List.of(),
                List.of(new ProfessionalServiceOffered(null, 999L))
        );

        when(professionalGateway.findByCpf(cpf.getValue())).thenReturn(Optional.empty());
        when(serviceOfferedGateway.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(professional))
                .isInstanceOf(ServiceOfferedNotFoundException.class)
                .hasMessageContaining("999");

        verify(professionalGateway, never()).save(any());
    }
}
