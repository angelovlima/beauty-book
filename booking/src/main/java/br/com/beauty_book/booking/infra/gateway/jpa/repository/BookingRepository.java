package br.com.beauty_book.booking.infra.gateway.jpa.repository;

import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.jpa.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingJpaEntity, Long>,
        JpaSpecificationExecutor<BookingJpaEntity> {

    List<BookingJpaEntity> findByProfessionalIdAndBookingDate(Long professionalId, LocalDate date);

    boolean existsByCustomerIdAndEstablishmentIdAndStatus(Long customerId, Long establishmentId, BookingStatus status);
}
