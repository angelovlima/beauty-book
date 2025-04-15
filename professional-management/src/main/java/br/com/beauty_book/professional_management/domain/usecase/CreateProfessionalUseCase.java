package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalAlreadyExistsException;
import br.com.beauty_book.professional_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.AvailabilityGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.gateway.ServiceOfferedGateway;
import br.com.beauty_book.professional_management.domain.model.Professional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProfessionalUseCase {

    private final ProfessionalGateway professionalGateway;
    private final AvailabilityGateway availabilityGateway;
    private final ProfessionalServiceOfferedGateway serviceGateway;
    private final ServiceOfferedGateway serviceOfferedGateway;

    public CreateProfessionalUseCase(ProfessionalGateway professionalGateway,
                                     AvailabilityGateway availabilityGateway,
                                     ProfessionalServiceOfferedGateway serviceGateway,
                                     ServiceOfferedGateway serviceOfferedGateway) {
        this.professionalGateway = professionalGateway;
        this.availabilityGateway = availabilityGateway;
        this.serviceGateway = serviceGateway;
        this.serviceOfferedGateway = serviceOfferedGateway;
    }

    @Transactional
    public Professional execute(Professional professional) {
        professionalGateway.findByCpf(professional.cpf().getValue())
                .ifPresent(existing -> {
                    throw new ProfessionalAlreadyExistsException(professional.cpf().getValue());
                });

        professional.services().forEach(service ->
                serviceOfferedGateway.findById(service.serviceOfferedId())
                        .orElseThrow(() -> new ServiceOfferedNotFoundException(
                                "Serviço com ID " + service.serviceOfferedId() + " não encontrado."))
        );

        Professional saved = professionalGateway.save(professional);

        availabilityGateway.saveAll(saved.id(), professional.availabilityList());
        serviceGateway.saveAll(saved.id(), professional.services());

        return saved;
    }
}
