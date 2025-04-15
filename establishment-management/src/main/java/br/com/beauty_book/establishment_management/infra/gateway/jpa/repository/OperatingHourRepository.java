package br.com.beauty_book.establishment_management.infra.gateway.jpa.repository;

import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.OperatingHourJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperatingHourRepository extends JpaRepository<OperatingHourJpaEntity, Long> {
    List<OperatingHourJpaEntity> findByEstablishmentId(Long establishmentId);
}
