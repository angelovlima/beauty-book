package br.com.beauty_book.professional_management.infra.gateway.jpa;

import br.com.beauty_book.professional_management.domain.model.Availability;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.mapper.AvailabilityJpaMapper;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.AvailabilityRepository;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AvailabilityJpaGateway.class, AvailabilityJpaMapper.class})
class AvailabilityJpaGatewayTest {

    @Autowired
    private AvailabilityJpaGateway gateway;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Test
    @DisplayName("should save and find all availabilities by professional ID")
    void shouldSaveAndFindByProfessionalId() {
        ProfessionalJpaEntity professional = professionalRepository.save(
                new ProfessionalJpaEntity(null, "Camila", "222.333.444-55", "11987654321", "camila@email.com")
        );

        Availability availability = new Availability(null, 1, LocalTime.of(8, 0), LocalTime.of(12, 0));
        gateway.saveAll(professional.getId(), List.of(availability));

        List<Availability> results = gateway.findAllByProfessionalId(professional.getId());

        assertThat(results).hasSize(1);
        assertThat(results.get(0).dayOfWeek()).isEqualTo(1);
    }

    @Test
    @DisplayName("should delete all availabilities by professional ID")
    void shouldDeleteByProfessionalId() {
        ProfessionalJpaEntity professional = professionalRepository.save(
                new ProfessionalJpaEntity(null, "Lucas", "555.444.333-22", "11999998888", "lucas@email.com")
        );

        Availability availability1 = new Availability(null, 1, LocalTime.of(8, 0), LocalTime.of(12, 0));
        Availability availability2 = new Availability(null, 2, LocalTime.of(14, 0), LocalTime.of(18, 0));
        gateway.saveAll(professional.getId(), List.of(availability1, availability2));

        assertThat(availabilityRepository.findAllByProfessionalId(professional.getId())).hasSize(2);

        gateway.deleteAllByProfessionalId(professional.getId());

        assertThat(availabilityRepository.findAllByProfessionalId(professional.getId())).isEmpty();
    }
}
