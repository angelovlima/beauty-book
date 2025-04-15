package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.api.dto.EstablishmentSearchFilter;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.EstablishmentJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ReviewRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ServiceOfferedRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({EstablishmentSearchJpaGateway.class, EstablishmentJpaMapper.class})
class EstablishmentSearchJpaGatewayTest {

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private ServiceOfferedRepository serviceRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EstablishmentSearchJpaGateway gateway;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        serviceRepository.deleteAll();
        establishmentRepository.deleteAll();

        var e1 = new EstablishmentJpaEntity("Studio A", "Centro", "url1");
        var e2 = new EstablishmentJpaEntity("Studio B", "Bairro", "url2");

        establishmentRepository.saveAll(List.of(e1, e2));

        serviceRepository.saveAll(List.of(
                new ServiceOfferedJpaEntity("Corte", "desc", new BigDecimal("50"), 45, e1),
                new ServiceOfferedJpaEntity("Manicure", "desc", new BigDecimal("30"), 30, e2)
        ));

        reviewRepository.saveAll(List.of(
                new ReviewJpaEntity(1L, e1, 5, "Excelente"),
                new ReviewJpaEntity(2L, e1, 4, "Muito bom"),
                new ReviewJpaEntity(3L, e2, 3, "Ok")
        ));
    }

    @Test
    @DisplayName("should return by name and location")
    void shouldSearchByNameAndLocation() {
        var filter = new EstablishmentSearchFilter("studio a", "centro", null, null, null, null, null);
        var result = gateway.search(filter);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Studio A");
    }

    @Test
    @DisplayName("should return by service name")
    void shouldSearchByServiceName() {
        var filter = new EstablishmentSearchFilter(null, null, "manicure", null, null, null, null);
        var result = gateway.search(filter);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Studio B");
    }

    @Test
    @DisplayName("should return by price range")
    void shouldSearchByPriceRange() {
        var filter = new EstablishmentSearchFilter(null, null, null, null, null, new BigDecimal("45"), new BigDecimal("55"));
        var result = gateway.search(filter);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Studio A");
    }

    @Test
    @DisplayName("should return all if no filter applied")
    void shouldReturnAllWhenNoFilter() {
        var filter = new EstablishmentSearchFilter(null, null, null, null, null, null, null);
        var result = gateway.search(filter);
        assertThat(result).hasSize(2);
    }
}
