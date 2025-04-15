package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.*;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.gateway.BookingNotifierGateway;
import br.com.beauty_book.booking.domain.gateway.EstablishmentGateway;
import br.com.beauty_book.booking.domain.gateway.ProfessionalGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class RescheduleBookingUseCase {

    private final BookingGateway bookingGateway;
    private final EstablishmentGateway establishmentGateway;
    private final ProfessionalGateway professionalGateway;
    private final BookingNotifierGateway notifierGateway;

    public Booking execute(Long bookingId, LocalDate newDate, LocalTime newStartTime) {
        Booking booking = bookingGateway.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Agendamento com ID " + bookingId + " não encontrado."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Agendamento já está cancelado.");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new CompletedBookingCannotBeCancelledException("Não é possível reagendar um agendamento finalizado.");
        }

        var duration = establishmentGateway.getServiceDuration(booking.getServiceId());
        var newEndTime = newStartTime.plusMinutes(duration);

        validateEstablishmentOperatingHour(booking.getEstablishmentId(), newDate, newStartTime);
        validateProfessionalAvailability(booking.getProfessionalId(), newDate, newStartTime);
        validateConflict(booking.getProfessionalId(), bookingId, newDate, newStartTime, newEndTime);

        booking.reschedule(newDate, newStartTime, newEndTime);
        Booking updated = bookingGateway.update(booking);

        notifierGateway.notifyBookingRescheduled(updated);
        return updated;
    }

    private void validateEstablishmentOperatingHour(Long establishmentId, LocalDate date, LocalTime time) {
        int dayOfWeek = date.getDayOfWeek().getValue() % 7;
        var hours = establishmentGateway.getOperatingHoursByEstablishmentId(establishmentId);
        var matching = hours.stream().filter(o -> o.dayOfWeek().equals(dayOfWeek)).toList();

        if (matching.isEmpty()) {
            throw new EstablishmentNotAvailableException("Estabelecimento não funciona neste dia da semana.");
        }

        boolean valid = matching.stream()
                .anyMatch(o -> !time.isBefore(o.startTime()) && time.isBefore(o.endTime()));

        if (!valid) {
            throw new EstablishmentNotAvailableException("Estabelecimento não está disponível neste horário.");
        }
    }

    private void validateProfessionalAvailability(Long professionalId, LocalDate date, LocalTime time) {
        int dayOfWeek = date.getDayOfWeek().getValue() % 7;
        var availability = professionalGateway.getAvailabilityByProfessionalId(professionalId);
        var matching = availability.stream().filter(a -> a.dayOfWeek().equals(dayOfWeek)).toList();

        if (matching.isEmpty()) {
            throw new ProfessionalNotAvailableException("Profissional não atende neste dia.");
        }

        boolean valid = matching.stream()
                .anyMatch(a -> !time.isBefore(a.startTime()) && time.isBefore(a.endTime()));

        if (!valid) {
            throw new ProfessionalNotAvailableException("Profissional não está disponível neste horário.");
        }
    }

    private void validateConflict(Long professionalId, Long currentBookingId, LocalDate date, LocalTime start, LocalTime end) {
        var existing = bookingGateway.findByProfessionalIdAndBookingDate(professionalId, date);
        boolean conflict = existing.stream()
                .filter(b -> !b.getId().equals(currentBookingId))
                .anyMatch(b -> start.isBefore(b.getEndTime()) && end.isAfter(b.getStartTime()));

        if (conflict) {
            throw new BookingConflictException("Horário conflitante com outro agendamento.");
        }
    }
}
