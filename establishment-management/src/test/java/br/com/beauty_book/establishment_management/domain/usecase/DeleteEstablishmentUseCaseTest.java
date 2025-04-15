package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteEstablishmentUseCaseTest {

    private EstablishmentGateway gateway;
    private DeleteEstablishmentUseCase useCase;

    @BeforeEach
    void setup() {
        gateway = mock(EstablishmentGateway.class);
        useCase = new DeleteEstablishmentUseCase(gateway);
    }

    @Test
    void shouldDeleteEstablishmentSuccessfully() {
        Long id = 1L;

        Establishment establishment = new Establishment(
                id, "Studio X", "Rua A", "foto.jpg",
                List.of(), List.of(), List.of()
        );

        when(gateway.findById(id)).thenReturn(Optional.of(establishment));

        useCase.execute(id);

        verify(gateway).deleteById(id);
    }

    @Test
    void shouldThrowWhenEstablishmentNotFound() {
        Long id = 2L;

        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(EstablishmentNotFoundException.class)
                .hasMessage("Establishment with ID 2 not found.");

        verify(gateway, never()).deleteById(any());
    }
}
