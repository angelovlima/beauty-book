package br.com.beauty_book.professional_management.infra.gateway.jpa.repository;

import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.AvailabilityJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilityJpaEntity, Long> {

    List<AvailabilityJpaEntity> findAllByProfessionalId(Long professionalId);

    void deleteAllByProfessionalId(Long professionalId);
}
