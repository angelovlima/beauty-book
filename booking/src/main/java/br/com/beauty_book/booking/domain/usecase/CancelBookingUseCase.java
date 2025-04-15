package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingAlreadyCancelledException;
import br.com.beauty_book.booking.domain.exception.BookingNotFoundException;
import br.com.beauty_book.booking.domain.exception.CompletedBookingCannotBeCancelledException;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.gateway.BookingNotifierGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelBookingUseCase {

    private final BookingGateway gateway;
    private final BookingNotifierGateway notifierGateway;

    public Booking execute(Long id) {
        Booking booking = gateway.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Agendamento com ID " + id + " não encontrado."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Agendamento já está cancelado.");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new CompletedBookingCannotBeCancelledException("Não é possível cancelar um agendamento finalizado.");
        }

        booking.cancel();
        Booking updated = gateway.update(booking);

        notifierGateway.notifyBookingCancelled(updated);
        return updated;
    }
}
