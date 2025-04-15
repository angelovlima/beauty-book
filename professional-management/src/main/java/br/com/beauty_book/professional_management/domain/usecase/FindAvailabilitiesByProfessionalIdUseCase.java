package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.model.Availability;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAvailabilitiesByProfessionalIdUseCase {

    private final AvailabilityGateway gateway;

    public FindAvailabilitiesByProfessionalIdUseCase(AvailabilityGateway gateway) {
        this.gateway = gateway;
    }

    public List<Availability> execute(Long professionalId) {
        return gateway.findAllByProfessionalId(professionalId);
    }
}
