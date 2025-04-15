package br.com.beauty_book.establishment_management.api.dto.mapper;

import br.com.beauty_book.establishment_management.api.dto.*;
import br.com.beauty_book.establishment_management.domain.model.Establishment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EstablishmentApiMapperTest {

    @Test
    void shouldMapCreateRequestToDomain() {
        CreateEstablishmentApiRequest request = new CreateEstablishmentApiRequest(
                "Studio X",
                "Rua Alpha, 123",
                "https://foto.com/x.jpg",
                List.of(new OperatingHourRequest(1, "09:00", "18:00")),
                List.of(new ServiceOfferedRequest("Corte", "Corte básico", 50.0, 30)),
                List.of(new ProfessionalIdRequest(1L))
        );

        Establishment result = EstablishmentApiMapper.toDomain(request);

        assertThat(result.getName()).isEqualTo("Studio X");
        assertThat(result.getOperatingHours()).hasSize(1);
        assertThat(result.getServices()).hasSize(1);
        assertThat(result.getProfessionals()).hasSize(1);
    }

    @Test
    void shouldMapUpdateRequestToDomain() {
        UpdateEstablishmentApiRequest request = new UpdateEstablishmentApiRequest(
                "Studio Y",
                "Rua Beta, 456",
                "https://foto.com/y.jpg",
                List.of(new OperatingHourRequest(2, "10:00", "17:00")),
                List.of(new ServiceOfferedRequest("Manicure", "Serviço completo", 80.0, 45)),
                List.of(new ProfessionalIdRequest(2L))
        );

        Establishment result = EstablishmentApiMapper.toDomain(request);

        assertThat(result.getName()).isEqualTo("Studio Y");
        assertThat(result.getServices().get(0).name()).isEqualTo("Manicure");
        assertThat(result.getProfessionals().get(0).professionalId()).isEqualTo(2L);
    }

    @Test
    void shouldMapDomainToResponse() {
        Establishment domain = new Establishment(
                10L,
                "Studio Z",
                "Rua Zeta, 999",
                "https://img.com/z.png",
                List.of(),
                List.of(),
                List.of()
        );

        EstablishmentApiResponse response = EstablishmentApiMapper.toResponse(domain);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Studio Z");
        assertThat(response.address()).isEqualTo("Rua Zeta, 999");
        assertThat(response.photoUrl()).isEqualTo("https://img.com/z.png");
    }
}
