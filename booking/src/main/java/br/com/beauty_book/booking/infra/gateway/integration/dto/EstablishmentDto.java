package br.com.beauty_book.booking.infra.gateway.integration.dto;

public record EstablishmentDto(
        Long id,
        String name,
        String address,
        String photoUrl
) {}
