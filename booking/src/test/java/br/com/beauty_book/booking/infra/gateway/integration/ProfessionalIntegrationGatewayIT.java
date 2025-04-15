package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.ProfessionalNotFoundException;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ProfessionalDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
@EnableFeignClients(basePackages = "br.com.beauty_book.booking.infra.gateway.integration.client")
@Import(ProfessionalIntegrationGateway.class)
public class ProfessionalIntegrationGatewayIT {

    @Container
    static final MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @Autowired
    private ProfessionalIntegrationGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockServerClient mockClient;

    @BeforeAll
    static void setupMockClient() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        System.setProperty("spring.cloud.discovery.client.simple.instances.professional-management[0].uri",
                mockServer.getEndpoint());
    }

    @Test
    void shouldFindProfessionalById() throws Exception {
        long id = 1L;
        ProfessionalDto expected = new ProfessionalDto(
                id,
                "Carla Souza",
                "12345678900",
                "11999999999",
                "carla@email.com"
        );

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/professionals/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expected)));

        Optional<ProfessionalDto> result = gateway.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(expected.id());
        assertThat(result.get().name()).isEqualTo(expected.name());
        assertThat(result.get().cpf()).isEqualTo(expected.cpf());
        assertThat(result.get().phoneNumber()).isEqualTo(expected.phoneNumber());
        assertThat(result.get().email()).isEqualTo(expected.email());
    }

    @Test
    void shouldThrowProfessionalNotFoundException() {
        long id = 999L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/professionals/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        ProfessionalNotFoundException ex = assertThrows(ProfessionalNotFoundException.class, () -> gateway.findById(id));
        assertThat(ex.getMessage()).isEqualTo("Profissional com ID " + id + " não encontrado.");
    }
}
