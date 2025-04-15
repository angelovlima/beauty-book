package br.com.beauty_book.professional_management.domain.model;

import java.time.LocalTime;

public record Availability(
        Long id,
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {}
