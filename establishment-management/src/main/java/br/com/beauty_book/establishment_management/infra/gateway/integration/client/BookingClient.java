package br.com.beauty_book.establishment_management.infra.gateway.integration.client;

import br.com.beauty_book.establishment_management.infra.gateway.integration.dto.BookingVerificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "booking",
        path = "/bookings"
)
public interface BookingClient {

    @GetMapping("/customer/{customerId}/has-completed/establishment/{establishmentId}")
    BookingVerificationDto hasCompletedBooking(
            @PathVariable("customerId") Long customerId,
            @PathVariable("establishmentId") Long establishmentId
    );
}
