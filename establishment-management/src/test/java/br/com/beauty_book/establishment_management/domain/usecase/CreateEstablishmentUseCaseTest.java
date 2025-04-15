package br.com.beauty_book.establishment_management.domain.usecase;

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

class CreateEstablishmentUseCaseTest {

    private EstablishmentGateway establishmentGateway;
    private ProfessionalGateway professionalGateway;
    private CreateEstablishmentUseCase useCase;

    @BeforeEach
    void setup() {
        establishmentGateway = mock(EstablishmentGateway.class);
        professionalGateway = mock(ProfessionalGateway.class);
        useCase = new CreateEstablishmentUseCase(establishmentGateway, professionalGateway);
    }

    @Test
    void shouldCreateEstablishmentSuccessfullyWhenAllProfessionalsExist() {
        var professionals = List.of(
                new ProfessionalEstablishment(null, 1L),
                new ProfessionalEstablishment(null, 2L)
        );

        var establishment = new Establishment(
                null,
                "Studio X",
                "Av. Paulista",
                "url.jpg",
                List.of(),
                List.of(),
                professionals
        );

        when(professionalGateway.findById(1L)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(2L)).thenReturn(Optional.of(mock()));
        when(establishmentGateway.save(establishment)).thenReturn(establishment);

        var result = useCase.execute(establishment);

        assertThat(result).isEqualTo(establishment);

        verify(professionalGateway).findById(1L);
        verify(professionalGateway).findById(2L);
        verify(establishmentGateway).save(establishment);
    }

    @Test
    void shouldThrowExceptionWhenAnyProfessionalDoesNotExist() {
        var professionals = List.of(
                new ProfessionalEstablishment(null, 1L),
                new ProfessionalEstablishment(null, 99L)
        );

        var establishment = new Establishment(
                null,
                "Studio Y",
                "Rua das Flores",
                "foto.jpg",
                List.of(),
                List.of(),
                professionals
        );

        when(professionalGateway.findById(1L)).thenReturn(Optional.of(mock()));
        when(professionalGateway.findById(99L)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> useCase.execute(establishment));

        assertThat(thrown)
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessage("Profissional com ID 99 não encontrado.");

        verify(professionalGateway).findById(1L);
        verify(professionalGateway).findById(99L);
        verify(establishmentGateway, never()).save(any());
    }
}
