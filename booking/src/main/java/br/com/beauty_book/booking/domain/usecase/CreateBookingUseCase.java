package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingConflictException;
import br.com.beauty_book.booking.domain.exception.EstablishmentNotAvailableException;
import br.com.beauty_book.booking.domain.exception.ProfessionalNotAvailableException;
import br.com.beauty_book.booking.domain.gateway.*;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class CreateBookingUseCase {

    private final BookingGateway bookingGateway;
    private final CustomerGateway customerGateway;
    private final ProfessionalGateway professionalGateway;
    private final EstablishmentGateway establishmentGateway;
    private final BookingNotifierGateway notifierGateway;

    public Booking execute(Booking booking) {
        validateEntitiesExist(booking);
        validateEstablishmentOperatingHour(booking);
        validateProfessionalAvailability(booking);

        Integer duration = establishmentGateway.getServiceDuration(booking.getServiceId());
        LocalTime endTime = booking.getStartTime().plusMinutes(duration);

        validateConflict(booking, endTime);

        Booking finalizedBooking = new Booking(
                null,
                booking.getCustomerId(),
                booking.getProfessionalId(),
                booking.getEstablishmentId(),
                booking.getServiceId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                endTime,
                BookingStatus.SCHEDULED
        );
        Booking saved = bookingGateway.save(finalizedBooking);

        notifierGateway.notifyBookingCreated(saved);
        return saved;
    }

    private void validateEntitiesExist(Booking booking) {
        customerGateway.findById(booking.getCustomerId());
        professionalGateway.findById(booking.getProfessionalId());
        establishmentGateway.findById(booking.getEstablishmentId());
        establishmentGateway.getServiceById(booking.getServiceId());
    }

    private void validateEstablishmentOperatingHour(Booking booking) {
        int dayOfWeek = booking.getBookingDate().getDayOfWeek().getValue() % 7;
        var operatingHours = establishmentGateway.getOperatingHoursByEstablishmentId(booking.getEstablishmentId());

        var hoursForDay = operatingHours.stream()
                .filter(o -> o.dayOfWeek().equals(dayOfWeek))
                .toList();

        if (hoursForDay.isEmpty()) {
            throw new EstablishmentNotAvailableException("Estabelecimento não funciona neste dia da semana.");
        }

        boolean isWithinOperatingHours = hoursForDay.stream()
                .anyMatch(o ->
                        !booking.getStartTime().isBefore(o.startTime()) &&
                                booking.getStartTime().isBefore(o.endTime())
                );

        if (!isWithinOperatingHours) {
            throw new EstablishmentNotAvailableException("Estabelecimento está fechado nesse horário.");
        }
    }


    private void validateProfessionalAvailability(Booking booking) {
        int dayOfWeek = booking.getBookingDate().getDayOfWeek().getValue() % 7;
        var availabilityList = professionalGateway.getAvailabilityByProfessionalId(booking.getProfessionalId());

        var availabilityForDay = availabilityList.stream()
                .filter(a -> a.dayOfWeek().equals(dayOfWeek))
                .toList();

        if (availabilityForDay.isEmpty()) {
            throw new ProfessionalNotAvailableException("Profissional não atende neste dia da semana.");
        }

        boolean isAvailable = availabilityForDay.stream()
                .anyMatch(a ->
                        !booking.getStartTime().isBefore(a.startTime()) &&
                                booking.getStartTime().isBefore(a.endTime())
                );

        if (!isAvailable) {
            throw new ProfessionalNotAvailableException("Profissional não está disponível neste horário.");
        }
    }

    private void validateConflict(Booking booking, LocalTime endTime) {
        var existingBookings = bookingGateway.findByProfessionalIdAndBookingDate(
                booking.getProfessionalId(),
                booking.getBookingDate()
        );

        boolean hasConflict = existingBookings.stream().anyMatch(existing ->
                booking.getStartTime().isBefore(existing.getEndTime()) &&
                        endTime.isAfter(existing.getStartTime())
        );

        if (hasConflict) {
            throw new BookingConflictException("Já existe um agendamento conflitante para esse horário.");
        }
    }
}
