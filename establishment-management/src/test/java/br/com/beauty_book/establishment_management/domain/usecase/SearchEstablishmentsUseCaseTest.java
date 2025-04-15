package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentSearchGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SearchEstablishmentsUseCaseTest {

    private EstablishmentSearchGateway searchGateway;
    private SearchEstablishmentsUseCase useCase;

    @BeforeEach
    void setUp() {
        searchGateway = mock(EstablishmentSearchGateway.class);
        useCase = new SearchEstablishmentsUseCase(searchGateway);
    }

    @Test
    void shouldReturnListOfEstablishmentsWhenFilterIsProvided() {
        EstablishmentSearchFilter filter = new EstablishmentSearchFilter(
                "Studio",
                "São Paulo",
                "Corte",
                4,
                1,
                new BigDecimal("30.00"),
                new BigDecimal("80.00")
        );

        List<Establishment> expected = List.of(
                new Establishment(1L, "Studio Top", "São Paulo", null, null, null, null)
        );

        when(searchGateway.search(filter)).thenReturn(expected);

        List<Establishment> result = useCase.execute(filter);

        assertThat(result).hasSize(1).containsExactlyElementsOf(expected);
        verify(searchGateway).search(filter);
    }
}
