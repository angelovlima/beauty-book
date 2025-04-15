package br.com.beauty_book.booking.domain.model;

import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
public class Booking {

    private final Long id;
    private final Long customerId;
    private final Long professionalId;
    private final Long establishmentId;
    private final Long serviceId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;

    public Booking(Long id,
                   Long customerId,
                   Long professionalId,
                   Long establishmentId,
                   Long serviceId,
                   LocalDate bookingDate,
                   LocalTime startTime,
                   LocalTime endTime,
                   BookingStatus status) {

        if (startTime != null && endTime != null && (startTime.isAfter(endTime) || startTime.equals(endTime))) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        this.id = id;
        this.customerId = Objects.requireNonNull(customerId, "customerId is required");
        this.professionalId = Objects.requireNonNull(professionalId, "professionalId is required");
        this.establishmentId = Objects.requireNonNull(establishmentId, "establishmentId is required");
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId is required");
        this.bookingDate = Objects.requireNonNull(bookingDate, "bookingDate is required");
        this.startTime = Objects.requireNonNull(startTime, "startTime is required");
        this.endTime = endTime;
        this.status = Objects.requireNonNull(status, "status is required");
    }

    public void cancel() {
        if (this.status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled.");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public void reschedule(LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime) {
        if (newStartTime.isAfter(newEndTime) || newStartTime.equals(newEndTime)) {
            throw new IllegalArgumentException("New start time must be before end time.");
        }
        if (newDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot reschedule to a past date.");
        }

        this.bookingDate = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.status = BookingStatus.RESCHEDULED;
    }


    public void complete() {
        if (this.status != BookingStatus.SCHEDULED && this.status != BookingStatus.RESCHEDULED) {
            throw new IllegalStateException("Only scheduled bookings can be completed.");
        }
        this.status = BookingStatus.COMPLETED;
    }
}
