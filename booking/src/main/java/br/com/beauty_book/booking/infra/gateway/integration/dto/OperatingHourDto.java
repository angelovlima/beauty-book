package br.com.beauty_book.booking.infra.gateway.integration.dto;

import java.time.LocalTime;

public record OperatingHourDto(
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {}
