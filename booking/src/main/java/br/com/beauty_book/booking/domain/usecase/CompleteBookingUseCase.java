package br.com.beauty_book.booking.domain.usecase;

import br.com.beauty_book.booking.domain.exception.BookingAlreadyCancelledException;
import br.com.beauty_book.booking.domain.exception.BookingAlreadyCompletedException;
import br.com.beauty_book.booking.domain.exception.BookingNotFoundException;
import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class CompleteBookingUseCase {

    private final BookingGateway bookingGateway;

    public CompleteBookingUseCase(BookingGateway bookingGateway) {
        this.bookingGateway = bookingGateway;
    }

    public Booking execute(Long id) {
        Booking booking = bookingGateway.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Agendamento com ID " + id + " não encontrado."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Não é possível finalizar um agendamento que foi cancelado.");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingAlreadyCompletedException("Agendamento já está finalizado.");
        }

        booking.complete();
        return bookingGateway.update(booking);
    }
}
