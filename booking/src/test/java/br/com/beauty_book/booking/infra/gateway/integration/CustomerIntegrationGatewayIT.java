package br.com.beauty_book.booking.infra.gateway.integration;

import br.com.beauty_book.booking.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.booking.infra.gateway.integration.dto.CustomerDto;
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
@Import(CustomerIntegrationGateway.class)
public class CustomerIntegrationGatewayIT {

    @Container
    static final MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @Autowired
    private CustomerIntegrationGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockServerClient mockClient;

    @BeforeAll
    static void setupMockClient() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        System.setProperty("spring.cloud.discovery.client.simple.instances.customer-management[0].uri",
                mockServer.getEndpoint());
    }

    @Test
    void shouldFindCustomerById() throws Exception {
        long id = 1L;
        CustomerDto expected = new CustomerDto(id, "Joana", "joana@email.com");

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/customers/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expected)));

        Optional<CustomerDto> result = gateway.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(expected.id());
        assertThat(result.get().name()).isEqualTo(expected.name());
        assertThat(result.get().email()).isEqualTo(expected.email());
    }

    @Test
    void shouldThrowCustomerNotFoundException() {
        long id = 999L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/customers/" + id))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class, () -> gateway.findById(id));
        assertThat(ex.getMessage()).isEqualTo("Cliente com ID " + id + " não encontrado.");
    }
}
