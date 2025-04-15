package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.gateway.ServiceOfferedGateway;
import br.com.beauty_book.establishment_management.domain.model.ServiceOffered;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.ServiceOfferedRepository;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.ServiceOfferedJpaMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceOfferedJpaGateway implements ServiceOfferedGateway {

    private final ServiceOfferedRepository repository;
    private final ServiceOfferedJpaMapper mapper;

    public ServiceOfferedJpaGateway(ServiceOfferedRepository repository,
                                    ServiceOfferedJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ServiceOffered> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
