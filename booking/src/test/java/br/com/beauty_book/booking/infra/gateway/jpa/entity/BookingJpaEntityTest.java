package br.com.beauty_book.booking.infra.gateway.jpa.entity;

import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingJpaEntityTest {

    @Test
    @DisplayName("should build BookingJpaEntity correctly using builder")
    void shouldBuildEntityCorrectly() {
        BookingJpaEntity entity = BookingJpaEntity.builder()
                .id(1L)
                .customerId(10L)
                .professionalId(20L)
                .establishmentId(30L)
                .serviceId(40L)
                .bookingDate(LocalDate.of(2025, 4, 25))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 45))
                .status(BookingStatus.SCHEDULED)
                .build();

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
    @DisplayName("should create BookingJpaEntity using all-args constructor")
    void shouldCreateEntityUsingConstructor() {
        BookingJpaEntity entity = new BookingJpaEntity(
                2L,
                11L,
                21L,
                31L,
                41L,
                LocalDate.of(2025, 5, 5),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                BookingStatus.RESCHEDULED
        );

        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getCustomerId()).isEqualTo(11L);
        assertThat(entity.getProfessionalId()).isEqualTo(21L);
        assertThat(entity.getEstablishmentId()).isEqualTo(31L);
        assertThat(entity.getServiceId()).isEqualTo(41L);
        assertThat(entity.getBookingDate()).isEqualTo(LocalDate.of(2025, 5, 5));
        assertThat(entity.getStartTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(entity.getEndTime()).isEqualTo(LocalTime.of(14, 30));
        assertThat(entity.getStatus()).isEqualTo(BookingStatus.RESCHEDULED);
    }
}
