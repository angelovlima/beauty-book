package br.com.beauty_book.professional_management.domain.gateway;

import br.com.beauty_book.professional_management.domain.model.Availability;

import java.util.List;

public interface AvailabilityGateway {

    void saveAll(Long professionalId, List<Availability> availabilityList);

    List<Availability> findAllByProfessionalId(Long professionalId);

    void deleteAllByProfessionalId(Long professionalId);
}
