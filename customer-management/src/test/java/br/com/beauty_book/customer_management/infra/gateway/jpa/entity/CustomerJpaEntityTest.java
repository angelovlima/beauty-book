package br.com.beauty_book.customer_management.infra.gateway.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJpaEntityTest {

    @Test
    @DisplayName("should build entity and access all fields via getters")
    void shouldBuildEntityAndAccessFields() {
        CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .id(1L)
                .name("Tiago Fernandes")
                .cpf("123.456.789-00")
                .phoneNumber("1199999-0000")
                .email("tiago.fernandes@example.com")
                .build();

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Tiago Fernandes");
        assertThat(entity.getCpf()).isEqualTo("123.456.789-00");
        assertThat(entity.getPhoneNumber()).isEqualTo("1199999-0000");
        assertThat(entity.getEmail()).isEqualTo("tiago.fernandes@example.com");
    }
}
