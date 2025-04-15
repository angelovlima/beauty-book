package br.com.beauty_book.booking.infra.gateway.jpa.mapper;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.jpa.entity.BookingJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingJpaMapperTest {

    private final BookingJpaMapper mapper = new BookingJpaMapper();

    @Test
    @DisplayName("should map Booking domain to BookingJpaEntity correctly")
    void shouldMapToEntityCorrectly() {
        Booking booking = new Booking(
                1L,
                10L,
                20L,
                30L,
                40L,
                LocalDate.of(2025, 4, 25),
                LocalTime.of(10, 0),
                LocalTime.of(10, 45),
                BookingStatus.SCHEDULED
        );

        BookingJpaEntity entity = mapper.toEntity(booking);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getCustomerId()).isEqualTo(10L);
        assertThat(entity.getProfessionalId()).isEqualTo(20L);
        assertThat(entity.getEstablishmentId()).isEqualTo(30L);
        assertThat(entity.getServiceId()).isEqualTo(40L);
        assertThat(entity.getBookingDate()).isEqualTo(LocalDate.of(2025, 4, 25));
        assertThat(entity.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(entity.getEndTime()).isEqualTo(LocalTime.of(10, 45));
        assertThat(entity.getStatus()).isEqualTo(BookingStatus.SCHEDULED);
    }

    @Test
    @DisplayName("should map BookingJpaEntity to Booking domain correctly")
    void shouldMapToDomainCorrectly() {
        BookingJpaEntity entity = BookingJpaEntity.builder()
                .id(2L)
                .customerId(11L)
                .professionalId(21L)
                .establishmentId(31L)
                .serviceId(41L)
                .bookingDate(LocalDate.of(2025, 5, 1))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(14, 30))
                .status(BookingStatus.RESCHEDULED)
                .build();

        Booking booking = mapper.toDomain(entity);

        assertThat(booking.getId()).isEqualTo(2L);
        assertThat(booking.getCustomerId()).isEqualTo(11L);
        assertThat(booking.getProfessionalId()).isEqualTo(21L);
        assertThat(booking.getEstablishmentId()).isEqualTo(31L);
        assertThat(booking.getServiceId()).isEqualTo(41L);
        assertThat(booking.getBookingDate()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(booking.getStartTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(booking.getEndTime()).isEqualTo(LocalTime.of(14, 30));
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.RESCHEDULED);
    }
}
