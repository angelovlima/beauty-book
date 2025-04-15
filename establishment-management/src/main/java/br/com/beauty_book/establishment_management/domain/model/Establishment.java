package br.com.beauty_book.establishment_management.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Establishment {

    private final Long id;
    private final String name;
    private final String address;
    private final String photoUrl;
    private final List<OperatingHour> operatingHours;
    private final List<ServiceOffered> services;
    private final List<ProfessionalEstablishment> professionals;

    public Establishment(Long id, String name, String address, String photoUrl,
                         List<OperatingHour> operatingHours,
                         List<ServiceOffered> services,
                         List<ProfessionalEstablishment> professionals) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoUrl = photoUrl;
        this.operatingHours = operatingHours;
        this.services = services;
        this.professionals = professionals;
    }
}
