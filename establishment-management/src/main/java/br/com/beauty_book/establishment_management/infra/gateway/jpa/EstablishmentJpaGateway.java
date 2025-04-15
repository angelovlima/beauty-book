package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.EstablishmentJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.EstablishmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EstablishmentJpaGateway implements EstablishmentGateway {

    private final EstablishmentRepository repository;

    public EstablishmentJpaGateway(EstablishmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Establishment save(Establishment establishment) {
        EstablishmentJpaEntity entity = EstablishmentJpaMapper.toEntity(establishment);
        EstablishmentJpaEntity saved = repository.save(entity);
        return EstablishmentJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Establishment> findById(Long id) {
        return repository.findById(id)
                .map(EstablishmentJpaMapper::toDomain);
    }

    @Override
    public List<Establishment> findAll() {
        return repository.findAll()
                .stream()
                .map(EstablishmentJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Establishment update(Long id, Establishment updated) {
        EstablishmentJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Establishment not found with ID " + id));

        entity.updateFromDomain(updated);

        EstablishmentJpaEntity saved = repository.save(entity);
        return EstablishmentJpaMapper.toDomain(saved);
    }

}
