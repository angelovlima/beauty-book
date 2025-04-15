package br.com.beauty_book.professional_management.infra.gateway.integration;

import br.com.beauty_book.professional_management.domain.exception.ServiceOfferedNotFoundException;
import br.com.beauty_book.professional_management.domain.model.ServiceOffered;
import br.com.beauty_book.professional_management.infra.gateway.integration.dto.ServiceOfferedDto;
import br.com.beauty_book.professional_management.infra.gateway.integration.mapper.ServiceOfferedDtoMapper;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
@EnableFeignClients(basePackages = "br.com.beauty_book.professional_management.infra.gateway.integration.client")
@Import({ServiceOfferedIntegrationGateway.class, ServiceOfferedDtoMapper.class})
@Testcontainers
class ServiceOfferedIntegrationGatewayIT {

    @Container
    static final MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    private static MockServerClient mockClient;

    @Autowired
    private ServiceOfferedIntegrationGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        System.setProperty(
                "spring.cloud.discovery.client.simple.instances.establishment-management[0].uri",
                mockServer.getEndpoint()
        );
    }

    @Test
    void shouldFindServiceById() throws Exception {
        long id = 1L;
        ServiceOfferedDto dto = new ServiceOfferedDto(
                id,
                "Corte Feminino",
                "Corte com lavagem e escova",
                new BigDecimal("49.90"),
                45
        );

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/services/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(dto)));

        Optional<ServiceOffered> result = gateway.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(id);
        assertThat(result.get().name()).isEqualTo("Corte Feminino");
    }

    @Test
    void shouldThrowExceptionWhenServiceNotFound() {
        long id = 999L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/services/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        ServiceOfferedNotFoundException exception = assertThrows(
                ServiceOfferedNotFoundException.class,
                () -> gateway.findById(id)
        );

        assertThat(exception.getMessage()).isEqualTo("Serviço com ID " + id + " não encontrado.");
    }
}
