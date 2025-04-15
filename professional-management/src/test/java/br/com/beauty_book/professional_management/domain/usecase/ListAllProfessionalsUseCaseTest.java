package br.com.beauty_book.professional_management.domain.usecase;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ListAllProfessionalsUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private AvailabilityGateway availabilityGateway;
    private ProfessionalServiceOfferedGateway serviceOfferedGateway;
    private ListAllProfessionalsUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        availabilityGateway = mock(AvailabilityGateway.class);
        serviceOfferedGateway = mock(ProfessionalServiceOfferedGateway.class);
        useCase = new ListAllProfessionalsUseCase(professionalGateway, availabilityGateway, serviceOfferedGateway);
    }

    @Test
    void shouldReturnAllProfessionalsWithDetails() {
        var professional1 = new Professional(1L, "João", new Cpf("111.111.111-11"), "+55 11 91111-1111", new Email("joao@example.com"), null, null);
        var professional2 = new Professional(2L, "Maria", new Cpf("222.222.222-22"), "+55 11 92222-2222", new Email("maria@example.com"), null, null);

        when(professionalGateway.findAll()).thenReturn(List.of(professional1, professional2));

        when(availabilityGateway.findAllByProfessionalId(1L)).thenReturn(List.of(
                new Availability(10L, 1, LocalTime.of(9, 0), LocalTime.of(12, 0))
        ));
        when(availabilityGateway.findAllByProfessionalId(2L)).thenReturn(List.of());

        when(serviceOfferedGateway.findAllByProfessionalId(1L)).thenReturn(List.of(
                new ProfessionalServiceOffered(101L, 1001L)
        ));
        when(serviceOfferedGateway.findAllByProfessionalId(2L)).thenReturn(List.of(
                new ProfessionalServiceOffered(102L, 1002L)
        ));

        var result = useCase.execute();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).availabilityList()).hasSize(1);
        assertThat(result.get(0).services()).hasSize(1);

        assertThat(result.get(1).availabilityList()).isEmpty();
        assertThat(result.get(1).services()).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenNoProfessionalsFound() {
        when(professionalGateway.findAll()).thenReturn(List.of());

        var result = useCase.execute();

        assertThat(result).isEmpty();
        verifyNoInteractions(availabilityGateway, serviceOfferedGateway);
    }
}
