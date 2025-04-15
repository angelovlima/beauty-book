package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.ServiceOfferedGateway;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetServiceOfferedByIdUseCaseTest {

    private ServiceOfferedGateway gateway;
    private GetServiceOfferedByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(ServiceOfferedGateway.class);
        useCase = new GetServiceOfferedByIdUseCase(gateway);
    }

    @Test
    void shouldReturnServiceOfferedWhenIdExists() {
        Long id = 1L;
        ServiceOffered expected = new ServiceOffered(id, "Corte Masculino", "Corte completo com tesoura e máquina", new BigDecimal("40.00"), 30);

        when(gateway.findById(id)).thenReturn(Optional.of(expected));

        ServiceOffered result = useCase.execute(id);

        assertThat(result).isEqualTo(expected);
        verify(gateway).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenServiceOfferedNotFound() {
        Long id = 2L;

        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(ServiceOfferedNotFoundException.class)
                .hasMessageContaining("Serviço com ID " + id + " não encontrado.");
    }
}
