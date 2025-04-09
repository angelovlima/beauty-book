package br.com.beauty_book.customer_management.infra.gateway.jpa.repository;

import br.com.beauty_book.customer_management.infra.gateway.jpa.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, Long> {
    Optional<CustomerJpaEntity> findByCpf(String cpf);
}
