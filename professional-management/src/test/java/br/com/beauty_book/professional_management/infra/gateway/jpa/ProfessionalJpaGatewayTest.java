package br.com.beauty_book.professional_management.infra.gateway.jpa;

import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import br.com.beauty_book.professional_management.infra.gateway.jpa.mapper.ProfessionalJpaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ProfessionalJpaGateway.class, ProfessionalJpaMapper.class})
class ProfessionalJpaGatewayTest {

    @Autowired
    private ProfessionalJpaGateway gateway;

    @Test
    @DisplayName("should save and retrieve professional by ID")
    void shouldSaveAndFindById() {
        Professional professional = new Professional(
                null,
                "Aline",
                new Cpf("111.222.333-44"),
                "11988887777",
                new Email("aline@email.com"),
                null,
                null
        );

        Professional saved = gateway.save(professional);
        Optional<Professional> found = gateway.findById(saved.id());

        assertThat(found).isPresent();
        assertThat(found.get().name()).isEqualTo("Aline");
    }

    @Test
    @DisplayName("should update professional")
    void shouldUpdateProfessional() {
        Professional original = new Professional(
                null,
                "Carlos",
                new Cpf("222.333.444-55"),
                "11999999999",
                new Email("carlos@email.com"),
                null,
                null
        );

        Professional saved = gateway.save(original);
        Professional updated = new Professional(
                saved.id(),
                "Carlos Lima",
                saved.cpf(),
                saved.phoneNumber(),
                saved.email(),
                null,
                null
        );

        Professional result = gateway.update(updated);
        assertThat(result.name()).isEqualTo("Carlos Lima");
    }

    @Test
    @DisplayName("should find professional by CPF")
    void shouldFindByCpf() {
        Professional professional = new Professional(
                null,
                "Fernanda",
                new Cpf("555.666.777-88"),
                "1188776655",
                new Email("fernanda@email.com"),
                null,
                null
        );

        gateway.save(professional);
        Optional<Professional> found = gateway.findByCpf("555.666.777-88");

        assertThat(found).isPresent();
        assertThat(found.get().name()).isEqualTo("Fernanda");
    }

    @Test
    @DisplayName("should delete professional by ID")
    void shouldDeleteById() {
        Professional professional = new Professional(
                null,
                "Marcos",
                new Cpf("999.888.777-66"),
                "1199001122",
                new Email("marcos@email.com"),
                null,
                null
        );

        Professional saved = gateway.save(professional);
        gateway.deleteById(saved.id());

        Optional<Professional> found = gateway.findById(saved.id());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("should list all professionals")
    void shouldListAll() {
        gateway.save(new Professional(null, "Joana", new Cpf("111.111.111-11"), "1111", new Email("joana@email.com"), null, null));
        gateway.save(new Professional(null, "Paulo", new Cpf("222.222.222-22"), "2222", new Email("paulo@email.com"), null, null));

        List<Professional> all = gateway.findAll();
        assertThat(all).hasSize(2);
    }
}
