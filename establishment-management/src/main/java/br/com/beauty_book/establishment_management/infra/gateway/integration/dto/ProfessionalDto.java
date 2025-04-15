package br.com.beauty_book.establishment_management.infra.gateway.integration.dto;

public record ProfessionalDto(
        Long id,
        String name,
        String cpf,
        String phoneNumber,
        String email
) {}
