package br.com.beauty_book.establishment_management.infra.gateway.jpa.entity;

import br.com.beauty_book.establishment_management.domain.model.Establishment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "establishment")
public class EstablishmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OperatingHourJpaEntity> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ServiceOfferedJpaEntity> services = new ArrayList<>();

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProfessionalEstablishmentJpaEntity> professionals = new ArrayList<>();

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewJpaEntity> reviews = new ArrayList<>();

    public EstablishmentJpaEntity(String name, String address, String photoUrl) {
        this.name = name;
        this.address = address;
        this.photoUrl = photoUrl;
    }

    public void updateFromDomain(Establishment updated) {
        this.name = updated.getName();
        this.address = updated.getAddress();
        this.photoUrl = updated.getPhotoUrl();

        this.operatingHours.clear();
        updated.getOperatingHours().forEach(o ->
                this.operatingHours.add(new OperatingHourJpaEntity(o.dayOfWeek(), o.startTime(), o.endTime(), this))
        );

        this.services.clear();
        updated.getServices().forEach(s ->
                this.services.add(new ServiceOfferedJpaEntity(s.name(), s.description(), s.price(), s.durationMinutes(), this))
        );

        this.professionals.clear();
        updated.getProfessionals().forEach(p ->
                this.professionals.add(new ProfessionalEstablishmentJpaEntity(p.professionalId(), this))
        );
    }

}
