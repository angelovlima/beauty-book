package br.com.beauty_book.establishment_management.infra.gateway.jpa.repository;

import br.com.beauty_book.establishment_management.infra.gateway.jpa.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewJpaEntity, Long> {

    Optional<ReviewJpaEntity> findByCustomerIdAndEstablishment_Id(Long customerId, Long establishmentId);

    List<ReviewJpaEntity> findAllByEstablishment_Id(Long establishmentId);
}
