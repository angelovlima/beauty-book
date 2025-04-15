
package br.com.beauty_book.booking.api.dto.mapper;

import br.com.beauty_book.booking.api.dto.BookingApiResponse;
import br.com.beauty_book.booking.api.dto.CreateBookingApiRequest;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.model.enums.BookingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingApiMapperTest {

    @Nested
    @DisplayName("Mapping from CreateBookingApiRequest to Booking domain")
    class FromCreateRequest {

        @Test
        @DisplayName("should map all fields correctly")
        void shouldMapCreateRequestToBooking() {
            CreateBookingApiRequest request = new CreateBookingApiRequest(
                    1L,
                    2L,
                    3L,
                    4L,
                    LocalDate.of(2025, 4, 25),
                    LocalTime.of(10, 0)
            );

            Booking result = BookingApiMapper.toDomain(request);

            assertThat(result.getId()).isNull();
            assertThat(result.getCustomerId()).isEqualTo(1L);
            assertThat(result.getProfessionalId()).isEqualTo(2L);
            assertThat(result.getEstablishmentId()).isEqualTo(3L);
            assertThat(result.getServiceId()).isEqualTo(4L);
            assertThat(result.getBookingDate()).isEqualTo(LocalDate.of(2025, 4, 25));
            assertThat(result.getStartTime()).isEqualTo(LocalTime.of(10, 0));
            assertThat(result.getEndTime()).isNull();
            assertThat(result.getStatus()).isEqualTo(BookingStatus.SCHEDULED);
        }
    }

    @Nested
    @DisplayName("Mapping from Booking domain to BookingApiResponse")
    class ToResponse {

        @Test
        @DisplayName("should map all fields correctly")
        void shouldMapBookingToResponse() {
            Booking booking = new Booking(
                    100L,
                    1L,
                    2L,
                    3L,
                    4L,
                    LocalDate.of(2025, 4, 25),
                    LocalTime.of(10, 0),
                    LocalTime.of(10, 45),
                    BookingStatus.SCHEDULED
            );

            BookingApiResponse response = BookingApiMapper.toResponse(booking);

            assertThat(response.id()).isEqualTo(100L);
            assertThat(response.customerId()).isEqualTo(1L);
            assertThat(response.professionalId()).isEqualTo(2L);
            assertThat(response.establishmentId()).isEqualTo(3L);
            assertThat(response.serviceId()).isEqualTo(4L);
            assertThat(response.bookingDate()).isEqualTo(LocalDate.of(2025, 4, 25));
            assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 0));
            assertThat(response.endTime()).isEqualTo(LocalTime.of(10, 45));
            assertThat(response.status()).isEqualTo(BookingStatus.SCHEDULED);
        }
    }
}
