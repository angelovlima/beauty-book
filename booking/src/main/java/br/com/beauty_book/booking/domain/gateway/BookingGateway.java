package br.com.beauty_book.booking.domain.gateway;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface BookingGateway {

    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    List<Booking> findByProfessionalIdAndBookingDate(Long professionalId, LocalDate bookingDate);

    List<Booking> findAllWithFilters(
            Long customerId,
            Long professionalId,
            Long establishmentId,
            LocalDate bookingDate
    );

    Booking update(Booking booking);

    boolean existsByCustomerIdAndEstablishmentIdAndStatus(Long customerId, Long establishmentId, BookingStatus status);
}