package br.com.beauty_book.professional_management.infra.gateway.jpa.repository;

import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalServiceOfferedJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalServiceOfferedRepository extends JpaRepository<ProfessionalServiceOfferedJpaEntity, Long> {

    List<ProfessionalServiceOfferedJpaEntity> findAllByProfessionalId(Long professionalId);

    void deleteAllByProfessionalId(Long professionalId);
}
