package br.com.beauty_book.professional_management.infra.gateway.jpa;

import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.model.Availability;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.AvailabilityJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.mapper.AvailabilityJpaMapper;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.AvailabilityRepository;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AvailabilityJpaGateway implements AvailabilityGateway {

    private final AvailabilityRepository availabilityRepository;
    private final ProfessionalRepository professionalRepository;
    private final AvailabilityJpaMapper mapper;

    public AvailabilityJpaGateway(AvailabilityRepository availabilityRepository,
                                  ProfessionalRepository professionalRepository,
                                  AvailabilityJpaMapper mapper) {
        this.availabilityRepository = availabilityRepository;
        this.professionalRepository = professionalRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void saveAll(Long professionalId, List<Availability> availabilityList) {
        ProfessionalJpaEntity professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));

        List<AvailabilityJpaEntity> entities = availabilityList.stream()
                .map(a -> mapper.toEntity(a, professional))
                .toList();

        availabilityRepository.saveAll(entities);
    }

    @Override
    public List<Availability> findAllByProfessionalId(Long professionalId) {
        return availabilityRepository.findAllByProfessionalId(professionalId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllByProfessionalId(Long professionalId) {
        availabilityRepository.deleteAllByProfessionalId(professionalId);
    }
}
