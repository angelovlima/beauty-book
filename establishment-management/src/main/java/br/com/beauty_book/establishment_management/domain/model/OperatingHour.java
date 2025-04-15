package br.com.beauty_book.establishment_management.domain.model;

import java.time.LocalTime;

public record OperatingHour(
        Long id,
        int dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {}
