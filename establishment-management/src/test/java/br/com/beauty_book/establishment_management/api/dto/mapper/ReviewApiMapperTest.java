package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.CreateReviewApiRequest;
import br.com.beauty_book.establishment_management.api.dto.ReviewApiResponse;
import br.com.beauty_book.establishment_management.domain.model.Review;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewApiMapperTest {

    @Test
    void shouldMapCreateRequestToDomain() {
        CreateReviewApiRequest request = new CreateReviewApiRequest(10L, 20L, 5, "Excelente serviço");

        Review result = ReviewApiMapper.toDomain(request);

        assertThat(result.getCustomerId()).isEqualTo(10L);
        assertThat(result.getEstablishmentId()).isEqualTo(20L);
        assertThat(result.getStars()).isEqualTo(5);
        assertThat(result.getComment()).isEqualTo("Excelente serviço");
    }

    @Test
    void shouldMapDomainToResponse() {
        Review review = new Review(99L, 10L, 20L, 4, "Muito bom");

        ReviewApiResponse response = ReviewApiMapper.toResponse(review);

        assertThat(response.id()).isEqualTo(99L);
        assertThat(response.customerId()).isEqualTo(10L);
        assertThat(response.establishmentId()).isEqualTo(20L);
        assertThat(response.stars()).isEqualTo(4);
        assertThat(response.comment()).isEqualTo("Muito bom");
    }
}
