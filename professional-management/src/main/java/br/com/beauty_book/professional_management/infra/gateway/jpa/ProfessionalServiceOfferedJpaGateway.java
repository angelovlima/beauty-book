package br.com.beauty_book.professional_management.infra.gateway.jpa;

import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.ProfessionalServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.mapper.ProfessionalServiceOfferedJpaMapper;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalRepository;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalServiceOfferedRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ProfessionalServiceOfferedJpaGateway implements ProfessionalServiceOfferedGateway {

    private final ProfessionalServiceOfferedRepository repository;
    private final ProfessionalRepository professionalRepository;
    private final ProfessionalServiceOfferedJpaMapper mapper;

    public ProfessionalServiceOfferedJpaGateway(ProfessionalServiceOfferedRepository repository,
                                                ProfessionalRepository professionalRepository,
                                                ProfessionalServiceOfferedJpaMapper mapper) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void saveAll(Long professionalId, List<ProfessionalServiceOffered> services) {
        ProfessionalJpaEntity professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found"));

        var entities = services.stream()
                .map(s -> mapper.toEntity(s, professional))
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public List<ProfessionalServiceOffered> findAllByProfessionalId(Long professionalId) {
        return repository.findAllByProfessionalId(professionalId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllByProfessionalId(Long professionalId) {
        repository.deleteAllByProfessionalId(professionalId);
    }
}
