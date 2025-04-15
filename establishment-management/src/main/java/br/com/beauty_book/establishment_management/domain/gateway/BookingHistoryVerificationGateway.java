package br.com.beauty_book.establishment_management.domain.gateway;

public interface BookingHistoryVerificationGateway {
    boolean customerHasCompletedBooking(Long customerId, Long establishmentId);
}
