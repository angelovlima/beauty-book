package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.model.Review;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.ReviewJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ReviewJpaGateway.class, ReviewJpaMapper.class})
class ReviewJpaGatewayTest {

    @Autowired
    private ReviewJpaGateway gateway;

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Long establishmentId;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        establishmentRepository.deleteAll();

        var establishment = new EstablishmentJpaEntity("Studio A", "Centro", "url");
        establishment = establishmentRepository.save(establishment);
        establishmentId = establishment.getId();
    }

    @Test
    @DisplayName("should save and retrieve review")
    void shouldSaveAndRetrieveReview() {
        var review = new Review(null, 1L, establishmentId, 5, "Excelente atendimento");
        var saved = gateway.save(review);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCustomerId()).isEqualTo(1L);
        assertThat(saved.getStars()).isEqualTo(5);
        assertThat(saved.getComment()).isEqualTo("Excelente atendimento");
    }

    @Test
    @DisplayName("should find review by customer and establishment")
    void shouldFindByCustomerAndEstablishment() {
        var review = new Review(null, 2L, establishmentId, 4, "Muito bom");
        gateway.save(review);

        Optional<Review> found = gateway.findByCustomerIdAndEstablishmentId(2L, establishmentId);

        assertThat(found).isPresent();
        assertThat(found.get().getStars()).isEqualTo(4);
        assertThat(found.get().getComment()).isEqualTo("Muito bom");
    }

    @Test
    @DisplayName("should find all reviews by establishment")
    void shouldFindAllByEstablishment() {
        var r1 = new Review(null, 3L, establishmentId, 3, "Bom");
        var r2 = new Review(null, 4L, establishmentId, 5, "Excelente");

        gateway.save(r1);
        gateway.save(r2);

        List<Review> reviews = gateway.findAllByEstablishmentId(establishmentId);

        assertThat(reviews).hasSize(2);
        assertThat(reviews).extracting(Review::getStars).containsExactlyInAnyOrder(3, 5);
    }
}
