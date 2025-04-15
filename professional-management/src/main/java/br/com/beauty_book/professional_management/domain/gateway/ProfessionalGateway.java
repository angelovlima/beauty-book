package br.com.beauty_book.professional_management.domain.gateway;

import br.com.beauty_book.professional_management.domain.model.Professional;

import java.util.List;
import java.util.Optional;

public interface ProfessionalGateway {

    Professional save(Professional professional);

    Professional update(Professional professional);

    void deleteById(Long id);

    Optional<Professional> findById(Long id);

    Optional<Professional> findByCpf(String cpf);

    List<Professional> findAll();
}
