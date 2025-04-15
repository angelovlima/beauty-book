package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalJpaMapper {

    public Professional toDomain(ProfessionalJpaEntity entity) {
        return new Professional(
                entity.getId(),
                entity.getName(),
                new Cpf(entity.getCpf()),
                entity.getPhoneNumber(),
                new Email(entity.getEmail()),
                null,
                null
        );
    }

    public ProfessionalJpaEntity toEntity(Professional domain) {
        return new ProfessionalJpaEntity(
                domain.id(),
                domain.name(),
                domain.cpf().getValue(),
                domain.phoneNumber(),
                domain.email().getValue()
        );
    }
}
