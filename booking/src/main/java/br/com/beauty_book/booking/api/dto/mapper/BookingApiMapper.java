package br.com.beauty_book.booking.api.dto.mapper;

import br.com.beauty_book.booking.api.dto.BookingApiResponse;
import br.com.beauty_book.booking.api.dto.CreateBookingApiRequest;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;

public class BookingApiMapper {

    private BookingApiMapper() {}

    public static Booking toDomain(CreateBookingApiRequest request) {
        return new Booking(
                null,
                request.customerId(),
                request.professionalId(),
                request.establishmentId(),
                request.serviceId(),
                request.bookingDate(),
                request.startTime(),
                null,
                BookingStatus.SCHEDULED
        );
    }

    public static BookingApiResponse toResponse(Booking booking) {
        return new BookingApiResponse(
                booking.getId(),
                booking.getCustomerId(),
                booking.getProfessionalId(),
                booking.getEstablishmentId(),
                booking.getServiceId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus()
        );
    }
}
