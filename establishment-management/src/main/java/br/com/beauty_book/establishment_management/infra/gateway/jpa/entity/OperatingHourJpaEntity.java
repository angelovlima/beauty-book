package br.com.beauty_book.establishment_management.infra.gateway.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "operating_hour")
public class OperatingHourJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id", nullable = false)
    private EstablishmentJpaEntity establishment;

    public OperatingHourJpaEntity(Integer dayOfWeek, LocalTime startTime, LocalTime endTime, EstablishmentJpaEntity establishment) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.establishment = establishment;
    }
}
