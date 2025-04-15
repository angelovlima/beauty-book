package br.com.beauty_book.booking.infra.gateway.jpa.mapper;

import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.infra.gateway.jpa.entity.BookingJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingJpaMapper {

    public BookingJpaEntity toEntity(Booking booking) {
        return BookingJpaEntity.builder()
                .id(booking.getId())
                .customerId(booking.getCustomerId())
                .professionalId(booking.getProfessionalId())
                .establishmentId(booking.getEstablishmentId())
                .serviceId(booking.getServiceId())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .build();
    }

    public Booking toDomain(BookingJpaEntity entity) {
        return new Booking(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProfessionalId(),
                entity.getEstablishmentId(),
                entity.getServiceId(),
                entity.getBookingDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus()
        );
    }
}
