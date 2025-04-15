package br.com.beauty_book.booking.infra.gateway.jpa;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.jpa.mapper.BookingJpaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookingJpaGateway.class, BookingJpaMapper.class})
class BookingJpaGatewayTest {

    @Autowired
    private BookingJpaGateway gateway;

    @Test
    @DisplayName("should save and retrieve booking by ID")
    void shouldSaveAndFindById() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 5, 1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                BookingStatus.SCHEDULED);

        Booking saved = gateway.save(booking);
        Optional<Booking> found = gateway.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCustomerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should find booking by professional and date")
    void shouldFindByProfessionalAndDate() {
        Booking booking = new Booking(null, 1L, 5L, 3L, 4L,
                LocalDate.of(2025, 5, 2),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                BookingStatus.SCHEDULED);

        gateway.save(booking);

        List<Booking> result = gateway.findByProfessionalIdAndBookingDate(5L, LocalDate.of(2025, 5, 2));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProfessionalId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("should filter bookings by criteria")
    void shouldFilterBookings() {
        Booking booking1 = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 5, 3),
                LocalTime.of(11, 0),
                LocalTime.of(11, 45),
                BookingStatus.SCHEDULED);
        Booking booking2 = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 5, 4),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                BookingStatus.SCHEDULED);

        gateway.save(booking1);
        gateway.save(booking2);

        List<Booking> filtered = gateway.findAllWithFilters(1L, 2L, 3L, LocalDate.of(2025, 5, 3));
        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).getBookingDate()).isEqualTo(LocalDate.of(2025, 5, 3));
    }

    @Test
    @DisplayName("should update a booking")
    void shouldUpdateBooking() {
        Booking booking = new Booking(null, 1L, 2L, 3L, 4L,
                LocalDate.of(2025, 5, 5),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0),
                BookingStatus.SCHEDULED);

        Booking saved = gateway.save(booking);
        Booking toUpdate = new Booking(saved.getId(), 1L, 2L, 3L, 4L,
                saved.getBookingDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                BookingStatus.COMPLETED);

        Booking updated = gateway.update(toUpdate);
        assertThat(updated.getStatus()).isEqualTo(BookingStatus.COMPLETED);
    }

    @Test
    @DisplayName("should verify existence of completed booking by customer and establishment")
    void shouldVerifyCompletedBookingExistence() {
        Booking booking = new Booking(null, 9L, 2L, 8L, 4L,
                LocalDate.of(2025, 5, 6),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                BookingStatus.COMPLETED);

        gateway.save(booking);

        boolean exists = gateway.existsByCustomerIdAndEstablishmentIdAndStatus(9L, 8L, BookingStatus.COMPLETED);
        assertThat(exists).isTrue();
    }
}
