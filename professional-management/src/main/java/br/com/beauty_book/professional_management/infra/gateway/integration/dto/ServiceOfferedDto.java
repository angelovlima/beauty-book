package br.com.beauty_book.professional_management.infra.gateway.integration.dto;

import java.math.BigDecimal;

public record ServiceOfferedDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes
) {}
