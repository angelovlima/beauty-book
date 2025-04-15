package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.api.dto.ListBookingsFilter;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListBookingsUseCase {

    private final BookingGateway gateway;

    public ListBookingsUseCase(BookingGateway gateway) {
        this.gateway = gateway;
    }

    public List<Booking> execute(ListBookingsFilter filter) {
        return gateway.findAllWithFilters(
                filter.customerId(),
                filter.professionalId(),
                filter.establishmentId(),
                filter.bookingDate()
        );
    }
}
