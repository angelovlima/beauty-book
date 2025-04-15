package br.com.beauty_book.booking.infra.gateway.integration.dto;

import java.math.BigDecimal;

public record ServiceOfferedDto(
        Long id,
        Long establishmentId,
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes
) {}
