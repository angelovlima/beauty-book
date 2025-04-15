package br.com.beauty_book.establishment_management.infra.gateway.integration;

import br.com.beauty_book.establishment_management.domain.gateway.BookingHistoryVerificationGateway;
import br.com.beauty_book.establishment_management.infra.gateway.integration.client.BookingClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingHistoryVerificationIntegrationGateway implements BookingHistoryVerificationGateway {

    private final BookingClient client;

    @Override
    public boolean customerHasCompletedBooking(Long customerId, Long establishmentId) {
        try {
            return client.hasCompletedBooking(customerId, establishmentId).hasCompletedBooking();
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
