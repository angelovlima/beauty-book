package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalJpaMapperTest {

    private final ProfessionalJpaMapper mapper = new ProfessionalJpaMapper();

    @Test
    void shouldConvertToDomain() {
        ProfessionalJpaEntity entity = new ProfessionalJpaEntity(
                1L,
                "Juliana Dias",
                "123.456.789-00",
                "11999999999",
                "juliana@email.com"
        );

        Professional result = mapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Juliana Dias");
        assertThat(result.cpf().getValue()).isEqualTo("123.456.789-00");
        assertThat(result.phoneNumber()).isEqualTo("11999999999");
        assertThat(result.email().getValue()).isEqualTo("juliana@email.com");
    }

    @Test
    void shouldConvertToEntity() {
        Professional domain = new Professional(
                2L,
                "Lucas Rocha",
                new Cpf("987.654.321-00"),
                "11888888888",
                new Email("lucas@email.com"),
                null,
                null
        );

        ProfessionalJpaEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getName()).isEqualTo("Lucas Rocha");
        assertThat(entity.getCpf()).isEqualTo("987.654.321-00");
        assertThat(entity.getPhoneNumber()).isEqualTo("11888888888");
        assertThat(entity.getEmail()).isEqualTo("lucas@email.com");
    }
}
