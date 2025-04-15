package br.com.beauty_book.booking.infra.gateway.jpa;

import br.com.beauty_book.booking.domain.gateway.BookingGateway;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import br.com.beauty_book.booking.infra.gateway.jpa.entity.BookingJpaEntity;
import br.com.beauty_book.booking.infra.gateway.jpa.mapper.BookingJpaMapper;
import br.com.beauty_book.booking.infra.gateway.jpa.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookingJpaGateway implements BookingGateway {

    private final BookingRepository repository;
    private final BookingJpaMapper mapper;

    @Override
    public Booking save(Booking booking) {
        return mapper.toDomain(repository.save(mapper.toEntity(booking)));
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByProfessionalIdAndBookingDate(Long professionalId, LocalDate bookingDate) {
        return repository.findByProfessionalIdAndBookingDate(professionalId, bookingDate)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findAllWithFilters(
            Long customerId,
            Long professionalId,
            Long establishmentId,
            LocalDate bookingDate
    ) {
        Specification<BookingJpaEntity> spec = Specification.where(null);

        if (customerId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("customerId"), customerId));
        }
        if (professionalId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("professionalId"), professionalId));
        }
        if (establishmentId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("establishmentId"), establishmentId));
        }
        if (bookingDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("bookingDate"), bookingDate));
        }

        return repository.findAll(spec).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Booking update(Booking booking) {
        return mapper.toDomain(repository.save(mapper.toEntity(booking)));
    }

    @Override
    public boolean existsByCustomerIdAndEstablishmentIdAndStatus(Long customerId, Long establishmentId, BookingStatus status) {
        return repository.existsByCustomerIdAndEstablishmentIdAndStatus(customerId, establishmentId, status);
    }
}
