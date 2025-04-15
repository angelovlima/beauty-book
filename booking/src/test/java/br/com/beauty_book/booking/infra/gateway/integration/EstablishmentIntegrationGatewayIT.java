package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.EstablishmentNotFoundException;
import br.com.beauty_book.booking.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.booking.infra.gateway.integration.dto.EstablishmentDto;
import br.com.beauty_book.booking.infra.gateway.integration.dto.ServiceOfferedDto;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
@EnableFeignClients(basePackages = "br.com.beauty_book.booking.infra.gateway.integration.client")
@Import(EstablishmentIntegrationGateway.class)
class EstablishmentIntegrationGatewayIT {

    @Container
    static final MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @Autowired
    private EstablishmentIntegrationGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockServerClient mockClient;

    @BeforeAll
    static void setupMockClient() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        System.setProperty("spring.cloud.discovery.client.simple.instances.establishment-management[0].uri",
                mockServer.getEndpoint());
    }

    @Test
    void shouldFindEstablishmentById() throws Exception {
        long id = 1L;
        EstablishmentDto expected = new EstablishmentDto(id, "Beleza Pura", "Av. Brasil", "https://example.com/foto.png");

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/establishments/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expected)));

        EstablishmentDto result = gateway.findById(id);

        assertThat(result.id()).isEqualTo(expected.id());
        assertThat(result.name()).isEqualTo(expected.name());
        assertThat(result.address()).isEqualTo(expected.address());
    }

    @Test
    void shouldThrowEstablishmentNotFoundException() {
        long id = 999L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/establishments/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        EstablishmentNotFoundException ex = assertThrows(
                EstablishmentNotFoundException.class,
                () -> gateway.findById(id)
        );

        assertThat(ex.getMessage()).isEqualTo("Estabelecimento com ID " + id + " não encontrado.");
    }

    @Test
    void shouldFindServiceOfferedById() throws Exception {
        long id = 10L;
        ServiceOfferedDto expected = new ServiceOfferedDto(
                id,
                1L,
                "Corte Feminino",
                "Corte básico",
                BigDecimal.valueOf(50.0),
                30
        );

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/services/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expected)));

        ServiceOfferedDto result = gateway.getServiceById(id);

        assertThat(result.id()).isEqualTo(expected.id());
        assertThat(result.name()).isEqualTo(expected.name());
        assertThat(result.description()).isEqualTo(expected.description());
        assertThat(result.price()).isEqualTo(expected.price());
        assertThat(result.durationMinutes()).isEqualTo(expected.durationMinutes());
    }

    @Test
    void shouldThrowServiceOfferedNotFoundException() {
        long id = 555L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/services/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        ServiceOfferedNotFoundException ex = assertThrows(
                ServiceOfferedNotFoundException.class,
                () -> gateway.getServiceById(id)
        );

        assertThat(ex.getMessage()).isEqualTo("Serviço com ID " + id + " não encontrado.");
    }
}
