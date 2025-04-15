package br.com.beauty_book.professional_management.domain.model;

import br.com.beauty_book.professional_management.domain.model.value_object.Cpf;
import br.com.beauty_book.professional_management.domain.model.value_object.Email;

import java.util.List;

public record Professional(
        Long id,
        String name,
        Cpf cpf,
        String phoneNumber,
        Email email,
        List<Availability> availabilityList,
        List<ProfessionalServiceOffered> services
) {}
