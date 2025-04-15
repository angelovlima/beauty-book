package br.com.beauty_book.establishment_management.infra.gateway.jpa.repository;

import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ServiceOfferedJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferedRepository extends JpaRepository<ServiceOfferedJpaEntity, Long> {
}
