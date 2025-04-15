package br.com.beauty_book.establishment_management.infra.gateway.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "professional_establishment")
public class ProfessionalEstablishmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id", nullable = false)
    private EstablishmentJpaEntity establishment;

    public ProfessionalEstablishmentJpaEntity(Long professionalId, EstablishmentJpaEntity establishment) {
        this.professionalId = professionalId;
        this.establishment = establishment;
    }
}
