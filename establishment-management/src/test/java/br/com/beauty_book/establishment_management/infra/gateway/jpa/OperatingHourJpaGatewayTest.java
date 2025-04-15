package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.OperatingHourJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.OperatingHourJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.OperatingHourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({OperatingHourJpaGateway.class, OperatingHourJpaMapper.class})
class OperatingHourJpaGatewayTest {

    @Autowired
    private EstablishmentRepository establishmentRepository;

    @Autowired
    private OperatingHourRepository operatingHourRepository;

    @Autowired
    private OperatingHourJpaGateway gateway;

    private Long establishmentId;

    @BeforeEach
    void setup() {
        operatingHourRepository.deleteAll();
        establishmentRepository.deleteAll();

        var establishment = new EstablishmentJpaEntity("Studio A", "Av. Central", "url");
        establishment = establishmentRepository.save(establishment);

        var oh1 = new OperatingHourJpaEntity(1, LocalTime.of(8, 0), LocalTime.of(18, 0), establishment);
        var oh2 = new OperatingHourJpaEntity(2, LocalTime.of(9, 0), LocalTime.of(17, 0), establishment);

        operatingHourRepository.saveAll(List.of(oh1, oh2));
        establishmentId = establishment.getId();
    }

    @Test
    @DisplayName("should return operating hours by establishment ID")
    void shouldReturnOperatingHoursByEstablishmentId() {
        List<OperatingHour> hours = gateway.findByEstablishmentId(establishmentId);

        assertThat(hours).hasSize(2);
        assertThat(hours.get(0).dayOfWeek()).isEqualTo(1);
        assertThat(hours.get(1).dayOfWeek()).isEqualTo(2);
    }
}
