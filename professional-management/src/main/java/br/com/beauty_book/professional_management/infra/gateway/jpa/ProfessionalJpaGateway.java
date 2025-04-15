package br.com.beauty_book.professional_management.infra.gateway.jpa;

import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.infra.gateway.jpa.mapper.ProfessionalJpaMapper;
import br.com.beauty_book.professional_management.infra.gateway.jpa.repository.ProfessionalRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ProfessionalJpaGateway implements ProfessionalGateway {

    private final ProfessionalRepository repository;
    private final ProfessionalJpaMapper mapper;

    public ProfessionalJpaGateway(ProfessionalRepository repository, ProfessionalJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Professional save(Professional professional) {
        return mapper.toDomain(repository.save(mapper.toEntity(professional)));
    }

    @Override
    @Transactional
    public Professional update(Professional professional) {
        return mapper.toDomain(repository.save(mapper.toEntity(professional)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Professional> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Professional> findByCpf(String cpf) {
        return repository.findByCpf(cpf).map(mapper::toDomain);
    }

    @Override
    public List<Professional> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
