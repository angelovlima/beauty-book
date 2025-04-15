package br.com.beauty_book.professional_management.domain.gateway;

import br.com.beauty_book.professional_management.domain.model.ProfessionalServiceOffered;

import java.util.List;

public interface ProfessionalServiceOfferedGateway {

    void saveAll(Long professionalId, List<ProfessionalServiceOffered> services);

    List<ProfessionalServiceOffered> findAllByProfessionalId(Long professionalId);

    void deleteAllByProfessionalId(Long professionalId);
}
