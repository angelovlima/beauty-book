package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ListEstablishmentsUseCaseTest {

    private EstablishmentGateway gateway;
    private ListEstablishmentsUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(EstablishmentGateway.class);
        useCase = new ListEstablishmentsUseCase(gateway);
    }

    @Test
    void shouldReturnListOfEstablishments() {
        List<Establishment> expected = List.of(
                new Establishment(1L, "Studio A", "Rua Alpha, 111", null, null, null, null),
                new Establishment(2L, "Studio B", "Rua Beta, 222", null, null, null, null)
        );

        when(gateway.findAll()).thenReturn(expected);

        List<Establishment> result = useCase.execute();

        assertThat(result).hasSize(2).containsExactlyElementsOf(expected);
        verify(gateway, times(1)).findAll();
    }
}
