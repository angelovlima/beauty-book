package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class BookingVerificationUseCase {

    private final BookingGateway bookingGateway;

    public BookingVerificationUseCase(BookingGateway bookingGateway) {
        this.bookingGateway = bookingGateway;
    }

    public boolean hasCompletedBookingByCustomerAndEstablishment(Long customerId, Long establishmentId) {
        return bookingGateway.existsByCustomerIdAndEstablishmentIdAndStatus(customerId, establishmentId, BookingStatus.COMPLETED);
    }
}
