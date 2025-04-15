package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.ServiceOfferedJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ServiceOfferedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ServiceOfferedJpaGateway.class, ServiceOfferedJpaMapper.class})
class ServiceOfferedJpaGatewayTest {

    @Autowired
    private ServiceOfferedJpaGateway gateway;

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private ServiceOfferedRepository serviceRepository;

    private Long serviceId;

    @BeforeEach
    void setup() {
        serviceRepository.deleteAll();
        establishmentRepository.deleteAll();

        var establishment = new EstablishmentJpaEntity("Studio A", "Centro", "url");
        establishment = establishmentRepository.save(establishment);

        var service = new ServiceOfferedJpaEntity(
                "Corte",
                "Corte feminino",
                new BigDecimal("50.00"),
                45,
                establishment
        );

        service = serviceRepository.save(service);
        serviceId = service.getId();
    }

    @Test
    @DisplayName("should find service by id")
    void shouldFindById() {
        Optional<ServiceOffered> result = gateway.findById(serviceId);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Corte");
        assertThat(result.get().price()).isEqualByComparingTo("50.00");
        assertThat(result.get().durationMinutes()).isEqualTo(45);
    }
}
