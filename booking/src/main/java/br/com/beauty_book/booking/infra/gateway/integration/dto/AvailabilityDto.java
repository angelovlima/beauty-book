package br.com.beauty_book.booking.infra.gateway.integration.dto;

import java.time.LocalTime;

public record AvailabilityDto(
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {}
