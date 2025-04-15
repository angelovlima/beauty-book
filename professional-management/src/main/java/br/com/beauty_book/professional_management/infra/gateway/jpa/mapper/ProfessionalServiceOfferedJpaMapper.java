package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.ProfessionalServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalServiceOfferedJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalServiceOfferedJpaMapper {

    public ProfessionalServiceOffered toDomain(ProfessionalServiceOfferedJpaEntity entity) {
        return new ProfessionalServiceOffered(entity.getId(), entity.getServiceOfferedId());
    }

    public ProfessionalServiceOfferedJpaEntity toEntity(ProfessionalServiceOffered domain, ProfessionalJpaEntity professional) {
        return new ProfessionalServiceOfferedJpaEntity(
                null,
                professional,
                domain.serviceOfferedId()
        );
    }
}
