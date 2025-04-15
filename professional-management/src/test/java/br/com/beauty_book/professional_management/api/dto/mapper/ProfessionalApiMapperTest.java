package br.com.beauty_book.professional_management.api.dto.mapper;

import br.com.beauty_book.professional_management.api.dto.*;
import br.com.beauty_book.professional_management.domain.model.Professional;
import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessionalApiMapperTest {

    @Test
    void shouldMapCreateRequestToDomain() {
        var request = new CreateProfessionalApiRequest(
                "Ana",
                "123.456.789-00",
                "+55 11 98888-0000",
                "ana@example.com",
                List.of(new AvailabilityRequest(1, "09:00", "12:00")),
                List.of(new ServiceOfferedIdRequest(10L))
        );

        Professional domain = ProfessionalApiMapper.toDomain(request);

        assertThat(domain.name()).isEqualTo("Ana");
        assertThat(domain.cpf().getValue()).isEqualTo("123.456.789-00");
        assertThat(domain.phoneNumber()).isEqualTo("+55 11 98888-0000");
        assertThat(domain.email().getValue()).isEqualTo("ana@example.com");

        assertThat(domain.availabilityList()).hasSize(1);
        assertThat(domain.availabilityList().get(0).dayOfWeek()).isEqualTo(1);
        assertThat(domain.services()).hasSize(1);
        assertThat(domain.services().get(0).serviceOfferedId()).isEqualTo(10L);
    }

    @Test
    void shouldMapUpdateRequestToDomain() {
        var request = new UpdateProfessionalApiRequest(
                "Lucas",
                "987.654.321-00",
                "+55 21 91234-5678",
                null,
                List.of(new AvailabilityRequest(2, "10:00", "14:00")),
                List.of(new ServiceOfferedIdRequest(15L))
        );

        Professional domain = ProfessionalApiMapper.toDomain(request, 99L);

        assertThat(domain.id()).isEqualTo(99L);
        assertThat(domain.name()).isEqualTo("Lucas");
        assertThat(domain.cpf()).isEqualTo(new Cpf("987.654.321-00"));
        assertThat(domain.email()).isNull();

        assertThat(domain.availabilityList().get(0).startTime().toString()).isEqualTo("10:00");
        assertThat(domain.services().get(0).serviceOfferedId()).isEqualTo(15L);
    }

    @Test
    void shouldMapDomainToResponse() {
        var domain = new Professional(
                5L,
                "Marcos",
                new Cpf("555.666.777-88"),
                "+55 47 98888-7777",
                new Email("marcos@ex.com"),
                List.of(),
                List.of()
        );

        ProfessionalApiResponse response = ProfessionalApiMapper.toResponse(domain);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.name()).isEqualTo("Marcos");
        assertThat(response.cpf()).isEqualTo("555.666.777-88");
        assertThat(response.phoneNumber()).isEqualTo("+55 47 98888-7777");
        assertThat(response.email()).isEqualTo("marcos@ex.com");
    }

    @Test
    void shouldHandleNullEmailSafely() {
        var domain = new Professional(
                6L,
                "Beatriz",
                new Cpf("111.222.333-44"),
                "+55 48 99999-4444",
                null,
                List.of(),
                List.of()
        );

        ProfessionalApiResponse response = ProfessionalApiMapper.toResponse(domain);

        assertThat(response.email()).isNull();
    }
}
