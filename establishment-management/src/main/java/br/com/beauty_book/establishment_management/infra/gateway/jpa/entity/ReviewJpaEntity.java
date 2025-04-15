package br.com.beauty_book.establishment_management.infra.gateway.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(name = "uk_customer_establishment", columnNames = {"customer_id", "establishment_id"})
})
public class ReviewJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "establishment_id", nullable = false)
    private EstablishmentJpaEntity establishment;

    @Column(nullable = false)
    private Integer stars;

    @Column(nullable = false)
    private String comment;

    public ReviewJpaEntity(Long customerId, EstablishmentJpaEntity establishment, Integer stars, String comment) {
        this.customerId = customerId;
        this.establishment = establishment;
        this.stars = stars;
        this.comment = comment;
    }
}
