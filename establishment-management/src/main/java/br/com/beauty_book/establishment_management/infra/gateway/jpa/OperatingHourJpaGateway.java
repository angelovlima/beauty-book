package br.com.beauty_book.establishment_management.infra.gateway.jpa;

import br.com.beauty_book.establishment_management.domain.gateway.OperatingHourGateway;
import br.com.beauty_book.establishment_management.domain.model.OperatingHour;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.mapper.OperatingHourJpaMapper;
import br.com.beauty_book.establishment_management.infra.gateway.jpa.repository.OperatingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OperatingHourJpaGateway implements OperatingHourGateway {

    private final OperatingHourRepository repository;
    private final OperatingHourJpaMapper mapper;

    @Override
    public List<OperatingHour> findByEstablishmentId(Long establishmentId) {
        return repository.findByEstablishmentId(establishmentId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
