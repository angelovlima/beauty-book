package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.BookingVerificationDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
@ActiveProfiles("test")
@EnableFeignClients(basePackages = "br.com.beauty_book.establishment_management.infra.gateway.integration.client")
@Import(BookingHistoryVerificationIntegrationGateway.class)
class BookingHistoryVerificationIntegrationGatewayIT {

    @Container
    static final MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @Autowired
    private BookingHistoryVerificationIntegrationGateway gateway;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockServerClient mockClient;

    @BeforeAll
    static void setupMockClient() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        System.setProperty(
                "spring.cloud.discovery.client.simple.instances.booking[0].uri",
                mockServer.getEndpoint()
        );
    }

    @Test
    void shouldReturnTrueWhenBookingCompleted() throws Exception {
        long customerId = 1L;
        long establishmentId = 2L;
        BookingVerificationDto response = new BookingVerificationDto(true);

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/bookings/customer/" + customerId + "/has-completed/establishment/" + establishmentId))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(response)));

        boolean result = gateway.customerHasCompletedBooking(customerId, establishmentId);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenNotFound() {
        long customerId = 1L;
        long establishmentId = 999L;

        mockClient.when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/bookings/customer/" + customerId + "/has-completed/establishment/" + establishmentId))
                .respond(HttpResponse.response()
                        .withStatusCode(404));

        boolean result = gateway.customerHasCompletedBooking(customerId, establishmentId);

        assertThat(result).isFalse();
    }
}
