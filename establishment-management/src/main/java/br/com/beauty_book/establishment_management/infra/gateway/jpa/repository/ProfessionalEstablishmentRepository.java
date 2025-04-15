package br.com.beauty_book.establishment_management.infra.gateway.jpa.repository;

import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ProfessionalEstablishmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalEstablishmentRepository extends JpaRepository<ProfessionalEstablishmentJpaEntity, Long> {
}
