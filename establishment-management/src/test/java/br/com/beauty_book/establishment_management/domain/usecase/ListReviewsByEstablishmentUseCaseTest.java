package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.gateway.ReviewGateway;
import br.com.beauty_book.establishment_management.domain.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ListReviewsByEstablishmentUseCaseTest {

    private ReviewGateway reviewGateway;
    private ListReviewsByEstablishmentUseCase useCase;

    @BeforeEach
    void setUp() {
        reviewGateway = mock(ReviewGateway.class);
        useCase = new ListReviewsByEstablishmentUseCase(reviewGateway);
    }

    @Test
    void shouldReturnListOfReviewsForEstablishment() {
        Long establishmentId = 200L;

        List<Review> expected = List.of(
                new Review(1L, 100L, establishmentId, 5, "Excelente"),
                new Review(2L, 101L, establishmentId, 4, "Bom atendimento")
        );

        when(reviewGateway.findAllByEstablishmentId(establishmentId)).thenReturn(expected);

        List<Review> result = useCase.execute(establishmentId);

        assertThat(result).hasSize(2).containsExactlyElementsOf(expected);
        verify(reviewGateway).findAllByEstablishmentId(establishmentId);
    }
}
