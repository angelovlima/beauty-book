package br.com.beauty_book.professional_management.domain.usecase;

import br.com.beauty_book.professional_management.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.professional_management.domain.gateway.ProfessionalGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProfessionalUseCase {

    private final ProfessionalGateway professionalGateway;

    public DeleteProfessionalUseCase(ProfessionalGateway professionalGateway) {
        this.professionalGateway = professionalGateway;
    }

    @Transactional
    public void execute(Long id) {
        professionalGateway.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));

        professionalGateway.deleteById(id);
    }
}
