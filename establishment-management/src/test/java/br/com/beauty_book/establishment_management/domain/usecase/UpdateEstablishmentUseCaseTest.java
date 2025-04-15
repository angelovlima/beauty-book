package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.establishment_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.domain.model.ProfessionalEstablishment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateEstablishmentUseCaseTest {

    private EstablishmentGateway establishmentGateway;
    private ProfessionalGateway professionalGateway;
    private UpdateEstablishmentUseCase useCase;

    @BeforeEach
    void setup() {
        establishmentGateway = mock(EstablishmentGateway.class);
        professionalGateway = mock(ProfessionalGateway.class);
        useCase = new UpdateEstablishmentUseCase(establishmentGateway, professionalGateway);
    }

    @Test
    void shouldUpdateEstablishmentSuccessfully() {
        var id = 1L;
        var professionals = List.of(
                new ProfessionalEstablishment(null, 10L),
                new ProfessionalEstablishment(null, 20L)
        );

        var updated = new Establishment(
                id, "Studio Novo", "Rua Nova", "nova-foto.jpg",
                List.of(), List.of(), professionals
        );

        when(establishmentGateway.findById(id)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(10L)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(20L)).thenReturn(Optional.of(mock()));
        when(establishmentGateway.update(id, updated)).thenReturn(updated);

        var result = useCase.execute(id, updated);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Studio Novo");
        assertThat(result.getAddress()).isEqualTo("Rua Nova");
    }

    @Test
    void shouldThrowWhenEstablishmentNotFound() {
        var id = 2L;

        when(establishmentGateway.findById(id)).thenReturn(Optional.empty());

        var input = new Establishment(
                id, "Studio", "Endereço", "foto.jpg",
                List.of(), List.of(), List.of()
        );

        assertThatThrownBy(() -> useCase.execute(id, input))
                .isInstanceOf(EstablishmentNotFoundException.class)
                .hasMessage("Establishment with ID 2 not found.");
    }

    @Test
    void shouldThrowWhenAnyProfessionalNotFound() {
        var id = 3L;

        var professionals = List.of(
                new ProfessionalEstablishment(null, 100L),
                new ProfessionalEstablishment(null, 999L)
        );

        var input = new Establishment(
                id, "Studio", "Endereço", "foto.jpg",
                List.of(), List.of(), professionals
        );

        when(establishmentGateway.findById(id)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(100L)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, input))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessage("Profissional com ID 999 não encontrado.");
    }
}
