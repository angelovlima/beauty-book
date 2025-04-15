package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteProfessionalUseCaseTest {

    private ProfessionalGateway professionalGateway;
    private DeleteProfessionalUseCase useCase;

    @BeforeEach
    void setUp() {
        professionalGateway = mock(ProfessionalGateway.class);
        useCase = new DeleteProfessionalUseCase(professionalGateway);
    }

    @Test
    void shouldDeleteProfessionalSuccessfully() {
        Long id = 1L;
        var professional = new Professional(
                id,
                "Carlos",
                new Cpf("123.456.789-00"),
                "+55 11 99999-9999",
                new Email("carlos@example.com"),
                null,
                null
        );

        when(professionalGateway.findById(id)).thenReturn(Optional.of(professional));

        useCase.execute(id);

        verify(professionalGateway).deleteById(id);
    }

    @Test
    void shouldThrowWhenProfessionalNotFound() {
        Long id = 99L;
        when(professionalGateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(ProfessionalNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

        verify(professionalGateway, never()).deleteById(any());
    }
}
