package br.com.beauty_book.establishment_management.domain.model;

import java.math.BigDecimal;

public record ServiceOffered(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int durationMinutes
) {}
