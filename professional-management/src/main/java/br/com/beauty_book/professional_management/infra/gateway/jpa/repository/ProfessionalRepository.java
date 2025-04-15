package br.com.beauty_book.professional_management.infra.gateway.jpa.repository;

import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<ProfessionalJpaEntity, Long> {
    Optional<ProfessionalJpaEntity> findByCpf(String cpf);
}
