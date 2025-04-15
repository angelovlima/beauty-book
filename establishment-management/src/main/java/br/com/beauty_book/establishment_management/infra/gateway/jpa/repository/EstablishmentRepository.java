package br.com.beauty_book.establishment_management.infra.gateway.jpa.repository;

import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.EstablishmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstablishmentRepository extends JpaRepository<EstablishmentJpaEntity, Long> {
}
