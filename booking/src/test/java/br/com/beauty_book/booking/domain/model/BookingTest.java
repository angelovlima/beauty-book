package br.com.beauty_book.booking.domain.model;

import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class BookingTest {

    @Test
    void shouldCreateBookingSuccessfully() {
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

        assertThat(booking.getCustomerId()).isEqualTo(10L);
        assertThat(booking.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(booking.getEndTime()).isEqualTo(LocalTime.of(10, 45));
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.SCHEDULED);
    }

    @Test
    void shouldThrowExceptionIfStartTimeEqualsOrAfterEndTime() {
        assertThatThrownBy(() ->
                new Booking(1L, 10L, 20L, 30L, 40L,
                        LocalDate.of(2025, 4, 25),
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 0),
                        BookingStatus.SCHEDULED)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start time must be before end time");
    }

    @Test
    void shouldCancelBookingSuccessfully() {
        Booking booking = buildScheduledBooking();
        booking.cancel();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void shouldThrowExceptionIfBookingAlreadyCancelled() {
        Booking booking = buildScheduledBooking();
        booking.cancel();
        assertThatThrownBy(booking::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void shouldRescheduleSuccessfully() {
        Booking booking = buildScheduledBooking();
        LocalDate newDate = LocalDate.now().plusDays(1);
        LocalTime newStart = LocalTime.of(11, 0);
        LocalTime newEnd = LocalTime.of(11, 45);

        booking.reschedule(newDate, newStart, newEnd);

        assertThat(booking.getBookingDate()).isEqualTo(newDate);
        assertThat(booking.getStartTime()).isEqualTo(newStart);
        assertThat(booking.getEndTime()).isEqualTo(newEnd);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.RESCHEDULED);
    }

    @Test
    void shouldThrowExceptionIfRescheduleToPastDate() {
        Booking booking = buildScheduledBooking();
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() ->
                booking.reschedule(pastDate, LocalTime.of(10, 0), LocalTime.of(10, 30))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("past date");
    }

    @Test
    void shouldThrowExceptionIfRescheduleWithInvalidTime() {
        Booking booking = buildScheduledBooking();
        LocalDate newDate = LocalDate.now().plusDays(1);

        assertThatThrownBy(() ->
                booking.reschedule(newDate, LocalTime.of(11, 0), LocalTime.of(10, 0))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("start time must be before");
    }

    @Test
    void shouldCompleteBookingIfScheduled() {
        Booking booking = buildScheduledBooking();
        booking.complete();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.COMPLETED);
    }

    @Test
    void shouldThrowExceptionIfCompletingAlreadyCancelled() {
        Booking booking = buildScheduledBooking();
        booking.cancel();

        assertThatThrownBy(booking::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("scheduled bookings can be completed");
    }

    private Booking buildScheduledBooking() {
        return new Booking(
                1L, 10L, 20L, 30L, 40L,
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                BookingStatus.SCHEDULED
        );
    }
}
