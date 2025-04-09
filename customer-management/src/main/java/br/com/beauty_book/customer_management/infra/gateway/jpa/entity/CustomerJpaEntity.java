package br.com.beauty_book.customer_management.infra.gateway.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String email;
}