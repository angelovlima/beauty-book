package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEstablishmentByIdUseCaseTest {

    private EstablishmentGateway gateway;
    private GetEstablishmentByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(EstablishmentGateway.class);
        useCase = new GetEstablishmentByIdUseCase(gateway);
    }

    @Test
    void shouldReturnEstablishmentWhenIdExists() {
        Long id = 1L;
        Establishment establishment = new Establishment(
                id,
                "Studio X",
                "Rua das Acácias, 123",
                null, null, null, null
        );

        when(gateway.findById(id)).thenReturn(Optional.of(establishment));

        Establishment result = useCase.execute(id);

        assertThat(result).isEqualTo(establishment);
        verify(gateway).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 2L;

        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(EstablishmentNotFoundException.class)
                .hasMessageContaining("Establishment with ID " + id + " not found");
    }
}
