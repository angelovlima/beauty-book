package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import br.com.beauty_book.establishment_management.domain.model.ProfessionalEstablishment;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.EstablishmentJpaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({EstablishmentJpaGateway.class})
class EstablishmentJpaGatewayTest {

    @Autowired
    private EstablishmentJpaGateway gateway;

    @Test
    @DisplayName("should save and retrieve establishment by ID")
    void shouldSaveAndFindById() {
        Establishment establishment = new Establishment(
                null,
                "Studio A",
                "Av. Central, 100",
                "https://example.com/img.jpg",
                List.of(new OperatingHour(null, 1, LocalTime.of(9, 0), LocalTime.of(18, 0))),
                List.of(new ServiceOffered(null, "Corte", "Corte feminino", new BigDecimal("50.00"), 45)),
                List.of(new ProfessionalEstablishment(null, 10L))
        );

        Establishment saved = gateway.save(establishment);
        Optional<Establishment> found = gateway.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Studio A");
    }

    @Test
    @DisplayName("should find all establishments")
    void shouldFindAll() {
        Establishment est1 = new Establishment(
                null,
                "Studio A",
                "Rua A",
                "https://img.com/a.jpg",
                List.of(),
                List.of(),
                List.of()
        );

        Establishment est2 = new Establishment(
                null,
                "Studio B",
                "Rua B",
                "https://img.com/b.jpg",
                List.of(),
                List.of(),
                List.of()
        );

        gateway.save(est1);
        gateway.save(est2);

        List<Establishment> all = gateway.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("should update establishment successfully")
    void shouldUpdate() {
        Establishment est = new Establishment(
                null,
                "Studio Original",
                "Rua X",
                "url1",
                List.of(),
                List.of(),
                List.of()
        );

        Establishment saved = gateway.save(est);

        Establishment updated = new Establishment(
                saved.getId(),
                "Studio Atualizado",
                "Rua Nova",
                "url2",
                List.of(),
                List.of(),
                List.of()
        );

        Establishment result = gateway.update(saved.getId(), updated);

        assertThat(result.getName()).isEqualTo("Studio Atualizado");
        assertThat(result.getAddress()).isEqualTo("Rua Nova");
        assertThat(result.getPhotoUrl()).isEqualTo("url2");
    }

    @Test
    @DisplayName("should delete establishment by ID")
    void shouldDeleteById() {
        Establishment est = new Establishment(
                null,
                "Studio Z",
                "Rua Z",
                "urlZ",
                List.of(),
                List.of(),
                List.of()
        );

        Establishment saved = gateway.save(est);
        gateway.deleteById(saved.getId());

        Optional<Establishment> result = gateway.findById(saved.getId());
        assertThat(result).isEmpty();
    }
}
