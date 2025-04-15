package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.domain.exception.CustomerNotFoundException;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.CustomerClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.CustomerDto;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerIntegrationGatewayTest {

    private CustomerClient client;
    private CustomerIntegrationGateway gateway;

    @BeforeEach
    void setUp() {
        client = mock(CustomerClient.class);
        gateway = new CustomerIntegrationGateway(client);
    }

    @Test
    @DisplayName("should return customer when found")
    void shouldReturnCustomerWhenFound() {
        CustomerDto expected = new CustomerDto(1L, "Carlos", "carlos@email.com");
        when(client.findById(1L)).thenReturn(expected);

        Optional<CustomerDto> result = gateway.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("should throw CustomerNotFoundException when customer is not found")
    void shouldThrowExceptionWhenCustomerNotFound() {
        FeignException.NotFound notFound = new FeignException.NotFound(
                "Not Found",
                Request.create(Request.HttpMethod.GET, "/customers/1", emptyMap(), null, StandardCharsets.UTF_8, null),
                null,
                null
        );
        when(client.findById(1L)).thenThrow(notFound);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> gateway.findById(1L));
        assertThat(exception.getMessage()).isEqualTo("Cliente com ID 1 não encontrado.");
    }
}
