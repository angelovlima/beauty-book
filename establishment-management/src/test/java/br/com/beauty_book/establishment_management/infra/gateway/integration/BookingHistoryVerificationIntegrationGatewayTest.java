package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.infra.gateway.integration.client.BookingClient;
import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.BookingVerificationDto;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingHistoryVerificationIntegrationGatewayTest {

    private BookingClient client;
    private BookingHistoryVerificationIntegrationGateway gateway;

    @BeforeEach
    void setUp() {
        client = mock(BookingClient.class);
        gateway = new BookingHistoryVerificationIntegrationGateway(client);
    }

    @Test
    @DisplayName("should return true when booking is completed")
    void shouldReturnTrueWhenBookingCompleted() {
        when(client.hasCompletedBooking(1L, 2L)).thenReturn(new BookingVerificationDto(true));

        boolean result = gateway.customerHasCompletedBooking(1L, 2L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when booking is not completed")
    void shouldReturnFalseWhenBookingNotCompleted() {
        when(client.hasCompletedBooking(1L, 2L)).thenReturn(new BookingVerificationDto(false));

        boolean result = gateway.customerHasCompletedBooking(1L, 2L);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return false when booking service returns 404")
    void shouldReturnFalseWhenFeignNotFound() {
        FeignException.NotFound notFound = new FeignException.NotFound(
                "Not Found",
                Request.create(Request.HttpMethod.GET, "/mock", Collections.emptyMap(), null, StandardCharsets.UTF_8, null),
                null,
                null
        );
        when(client.hasCompletedBooking(1L, 2L)).thenThrow(notFound);

        boolean result = gateway.customerHasCompletedBooking(1L, 2L);

        assertThat(result).isFalse();
    }
}
