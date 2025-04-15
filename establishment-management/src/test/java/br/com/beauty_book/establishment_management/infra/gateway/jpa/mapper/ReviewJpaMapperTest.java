package br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.establishment_management.domain.model.Review;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewJpaMapperTest {

    private final ReviewJpaMapper mapper = new ReviewJpaMapper();

    @Test
    @DisplayName("should map Review domain to ReviewJpaEntity correctly")
    void shouldMapToEntityCorrectly() {
        var establishment = new EstablishmentJpaEntity("Studio A", "Rua A", "https://example.com/foto.jpg");
        ReflectionTestUtils.setField(establishment, "id", 10L);

        var domain = new Review(null, 1L, 10L, 5, "Excelente atendimento");

        ReviewJpaEntity entity = mapper.toEntity(domain, establishment);

        assertThat(entity.getCustomerId()).isEqualTo(1L);
        assertThat(entity.getEstablishment().getId()).isEqualTo(10L);
        assertThat(entity.getStars()).isEqualTo(5);
        assertThat(entity.getComment()).isEqualTo("Excelente atendimento");
    }

    @Test
    @DisplayName("should map ReviewJpaEntity to Review domain correctly")
    void shouldMapToDomainCorrectly() {
        var establishment = new EstablishmentJpaEntity("Studio B", "Rua B", "https://example.com/img.jpg");
        ReflectionTestUtils.setField(establishment, "id", 20L);

        var entity = new ReviewJpaEntity(2L, establishment, 4, "Bom serviço");
        ReflectionTestUtils.setField(entity, "id", 100L);

        Review domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(100L);
        assertThat(domain.getCustomerId()).isEqualTo(2L);
        assertThat(domain.getEstablishmentId()).isEqualTo(20L);
        assertThat(domain.getStars()).isEqualTo(4);
        assertThat(domain.getComment()).isEqualTo("Bom serviço");
    }
}
