package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.Professional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllProfessionalsUseCase {

    private final ProfessionalGateway professionalGateway;
    private final AvailabilityGateway availabilityGateway;
    private final ProfessionalServiceOfferedGateway serviceOfferedGateway;

    public ListAllProfessionalsUseCase(ProfessionalGateway professionalGateway,
                                       AvailabilityGateway availabilityGateway,
                                       ProfessionalServiceOfferedGateway serviceOfferedGateway) {
        this.professionalGateway = professionalGateway;
        this.availabilityGateway = availabilityGateway;
        this.serviceOfferedGateway = serviceOfferedGateway;
    }

    public List<Professional> execute() {
        return professionalGateway.findAll().stream()
                .map(basic -> new Professional(
                        basic.id(),
                        basic.name(),
                        basic.cpf(),
                        basic.phoneNumber(),
                        basic.email(),
                        availabilityGateway.findAllByProfessionalId(basic.id()),
                        serviceOfferedGateway.findAllByProfessionalId(basic.id())
                ))
                .toList();
    }
}
