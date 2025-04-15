package br.com.beauty_book.professional_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.professional_management.domain.model.ProfessionalServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalJpaEntity;
import br.com.beauty_book.professional_management.infra.gateway.jpa.entity.ProfessionalServiceOfferedJpaEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalServiceOfferedJpaMapperTest {

    private final ProfessionalServiceOfferedJpaMapper mapper = new ProfessionalServiceOfferedJpaMapper();

    @Test
    void shouldConvertToDomain() {
        ProfessionalServiceOfferedJpaEntity entity = new ProfessionalServiceOfferedJpaEntity(
                10L,
                new ProfessionalJpaEntity(1L, "Ana", "123.456.789-00", "11999999999", "ana@email.com"),
                5L
        );

        ProfessionalServiceOffered result = mapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.serviceOfferedId()).isEqualTo(5L);
    }

    @Test
    void shouldConvertToEntity() {
        ProfessionalServiceOffered domain = new ProfessionalServiceOffered(null, 7L);

        ProfessionalJpaEntity professional = new ProfessionalJpaEntity(
                2L,
                "Carlos",
                "987.654.321-00",
                "11988888888",
                "carlos@email.com"
        );

        ProfessionalServiceOfferedJpaEntity entity = mapper.toEntity(domain, professional);

        assertThat(entity).isNotNull();
        assertThat(entity.getProfessional()).isEqualTo(professional);
        assertThat(entity.getServiceOfferedId()).isEqualTo(7L);
    }
}
