package br.com.beauty_book.booking.api.controller;

import br.com.beauty_book.booking.api.dto.*;
import br.com.beauty_book.booking.api.dto.mapper.BookingApiMapper;
import br.com.beauty_book.booking.domain.model.Booking;
import br.com.beauty_book.booking.domain.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final ListBookingsUseCase listBookingsUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final RescheduleBookingUseCase rescheduleBookingUseCase;
    private final CompleteBookingUseCase completeBookingUseCase;
    private final BookingVerificationUseCase bookingVerificationUseCase;

    @Operation(
            summary = "Create Booking",
            description = "Schedules a new booking with validation of availability, duration and conflicts.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload for creating a booking",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Create Booking Example",
                                    value = """
                                {
                                  "customerId": 1,
                                  "professionalId": 2,
                                  "establishmentId": 3,
                                  "serviceId": 4,
                                  "bookingDate": "2025-04-25",
                                  "startTime": "15:00"
                                }
                                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Booking successfully created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input or unavailable schedule"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflicting booking"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BookingApiResponse> create(@RequestBody @Valid CreateBookingApiRequest request) {
        Booking booking = BookingApiMapper.toDomain(request);
        Booking saved = createBookingUseCase.execute(booking);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BookingApiMapper.toResponse(saved));
    }

    @Operation(
            summary = "List Bookings",
            description = "Returns a list of bookings. Filters by customerId, professionalId, establishmentId, or bookingDate are optional.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of bookings",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookingApiResponse.class)),
                                    examples = @ExampleObject(
                                            name = "Booking List",
                                            value = """
                                        [
                                          {
                                            "id": 1,
                                            "customerId": 1,
                                            "professionalId": 3,
                                            "establishmentId": 5,
                                            "serviceId": 10,
                                            "bookingDate": "2025-04-20",
                                            "startTime": "10:00",
                                            "endTime": "10:45",
                                            "status": "SCHEDULED"
                                          },
                                          {
                                            "id": 2,
                                            "customerId": 2,
                                            "professionalId": 4,
                                            "establishmentId": 6,
                                            "serviceId": 12,
                                            "bookingDate": "2025-04-21",
                                            "startTime": "13:00",
                                            "endTime": "13:30",
                                            "status": "SCHEDULED"
                                          }
                                        ]
                                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BookingApiResponse>> findAll(ListBookingsFilter filter) {
        List<Booking> bookings = listBookingsUseCase.execute(filter);

        List<BookingApiResponse> response = bookings.stream()
                .map(BookingApiMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Cancel Booking",
            description = "Cancels a booking by ID, changing its status to CANCELLED if it's not already completed or cancelled.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking successfully cancelled",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Cancelled Booking",
                                            value = """
                                        {
                                          "id": 1,
                                          "customerId": 1,
                                          "professionalId": 3,
                                          "establishmentId": 5,
                                          "serviceId": 10,
                                          "bookingDate": "2025-04-20",
                                          "startTime": "10:00",
                                          "endTime": "10:45",
                                          "status": "CANCELLED"
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found",
                                            value = """
                                        {
                                          "message": "Agendamento com ID 99 não encontrado."
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Booking cannot be cancelled (already completed or cancelled)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Invalid Cancel",
                                            value = """
                                        {
                                          "message": "Agendamento já está cancelado."
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BookingApiResponse> cancel(@PathVariable Long id) {
        var cancelled = cancelBookingUseCase.execute(id);
        return ResponseEntity.ok(BookingApiMapper.toResponse(cancelled));
    }

    @PatchMapping("/{id}/reschedule")
    @Operation(
            summary = "Reschedule Booking",
            description = "Re-schedules an existing booking with a new date and start time.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New date and time for the booking",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Reagendamento válido",
                                    value = """
                {
                  "newBookingDate": "2025-04-25",
                  "newStartTime": "15:00"
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking successfully rescheduled"),
                    @ApiResponse(responseCode = "404", description = "Booking not found"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    public ResponseEntity<BookingApiResponse> reschedule(
            @PathVariable Long id,
            @RequestBody @Valid RescheduleBookingApiRequest request
    ) {
        var updated = rescheduleBookingUseCase.execute(id, request.newBookingDate(), request.newStartTime());
        return ResponseEntity.ok(BookingApiMapper.toResponse(updated));
    }

    @PatchMapping("/{id}/complete")
    @Operation(
            summary = "Complete Booking",
            description = "Marks a booking as COMPLETED. Only scheduled or rescheduled bookings can be finalized.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking successfully finalized",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Finalized Booking",
                                            value = """
                                        {
                                          "id": 1,
                                          "customerId": 1,
                                          "professionalId": 3,
                                          "establishmentId": 5,
                                          "serviceId": 10,
                                          "bookingDate": "2025-04-25",
                                          "startTime": "15:00",
                                          "endTime": "15:45",
                                          "status": "COMPLETED"
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Booking not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found",
                                            value = """
                                        {
                                          "message": "Agendamento com ID 99 não encontrado."
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Cannot finalize booking",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Already Completed or Cancelled",
                                            value = """
                                        {
                                          "message": "Agendamento já está finalizado."
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BookingApiResponse> complete(@PathVariable Long id) {
        var completed = completeBookingUseCase.execute(id);
        return ResponseEntity.ok(BookingApiMapper.toResponse(completed));
    }

    @GetMapping("/customer/{customerId}/has-completed/establishment/{establishmentId}")
    @Operation(
            summary = "Check if customer has completed bookings",
            description = "Returns true if the customer has at least one booking with status COMPLETED.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Boolean indicating whether customer has completed bookings",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Customer has completed",
                                            value = """
                    {
                      "hasCompletedBooking": true
                    }
                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<BookingVerificationApiResponse> hasCompletedBookingByCustomerAndEstablishment(
            @PathVariable Long customerId,
            @PathVariable Long establishmentId
    ) {
        boolean hasCompleted = bookingVerificationUseCase.hasCompletedBookingByCustomerAndEstablishment(customerId, establishmentId);
        return ResponseEntity.ok(new BookingVerificationApiResponse(hasCompleted));
    }


}

