package br.com.beauty_book.professional_management.infra.gateway.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professional_service_offered")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalServiceOfferedJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private ProfessionalJpaEntity professional;

    @Column(name = "service_offered_id", nullable = false)
    private Long serviceOfferedId;
}
