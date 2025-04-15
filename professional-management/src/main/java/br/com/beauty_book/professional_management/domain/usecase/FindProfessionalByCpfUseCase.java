package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import org.springframework.stereotype.Service;

@Service
public class FindProfessionalByCpfUseCase {

    private final ProfessionalGateway professionalGateway;
    private final AvailabilityGateway availabilityGateway;
    private final ProfessionalServiceOfferedGateway serviceOfferedGateway;

    public FindProfessionalByCpfUseCase(ProfessionalGateway professionalGateway,
                                        AvailabilityGateway availabilityGateway,
                                        ProfessionalServiceOfferedGateway serviceOfferedGateway) {
        this.professionalGateway = professionalGateway;
        this.availabilityGateway = availabilityGateway;
        this.serviceOfferedGateway = serviceOfferedGateway;
    }

    public Professional execute(Cpf cpf) {
        Professional basic = professionalGateway.findByCpf(cpf.getValue())
                .orElseThrow(() -> new ProfessionalNotFoundException(cpf.getValue()));

        return new Professional(
                basic.id(),
                basic.name(),
                basic.cpf(),
                basic.phoneNumber(),
                basic.email(),
                availabilityGateway.findAllByProfessionalId(basic.id()),
                serviceOfferedGateway.findAllByProfessionalId(basic.id())
        );
    }
}
