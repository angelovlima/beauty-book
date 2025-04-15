package br.com.beauty_book.establishment_management.domain.usecase;

import br.com.beauty_book.establishment_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.establishment_management.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.establishment_management.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.springframework.stereotype.Service;

@Service
public class CreateEstablishmentUseCase {

    private final EstablishmentGateway gateway;
    private final ProfessionalGateway professionalGateway;

    public CreateEstablishmentUseCase(EstablishmentGateway gateway,
                                      ProfessionalGateway professionalGateway) {
        this.gateway = gateway;
        this.professionalGateway = professionalGateway;
    }

    public Establishment execute(Establishment establishment) {
        establishment.getProfessionals().forEach(professional -> {
            professionalGateway.findById(professional.professionalId())
                    .orElseThrow(() -> new ProfessionalNotFoundException(
                            "Profissional com ID " + professional.professionalId() + " não encontrado."));
        });

        return gateway.save(establishment);
    }
}
