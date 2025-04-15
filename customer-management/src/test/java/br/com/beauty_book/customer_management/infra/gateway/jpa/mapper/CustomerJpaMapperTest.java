package br.com.beauty_book.customer_management.infra.gateway.jpa.mapper;

import br.com.beauty_book.customer_management.domain.model.Customer;
import br.com.beauty_book.customer_management.domain.model.value_object.Cpf;
import br.com.beauty_book.customer_management.domain.model.value_object.Email;
import br.com.beauty_book.customer_management.infra.gateway.jpa.entity.CustomerJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJpaMapperTest {

    @Nested
    @DisplayName("toDomain mapping")
    class ToDomain {

        @Test
        void shouldMapEntityToDomainWithEmail() {
            CustomerJpaEntity entity = CustomerJpaEntity.builder()
                    .id(1L)
                    .name("Marcos Lima")
                    .cpf("123.456.789-00")
                    .phoneNumber("1199999-9999")
                    .email("marcos.lima@example.com")
                    .build();

            Customer result = CustomerJpaMapper.toDomain(entity);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Marcos Lima");
            assertThat(result.getCpf().getValue()).isEqualTo("123.456.789-00");
            assertThat(result.getPhoneNumber()).isEqualTo("1199999-9999");
            assertThat(result.getEmail().getValue()).isEqualTo("marcos.lima@example.com");
        }

        @Test
        void shouldMapEntityToDomainWithoutEmail() {
            CustomerJpaEntity entity = CustomerJpaEntity.builder()
                    .id(2L)
                    .name("Fernanda Rocha")
                    .cpf("987.654.321-00")
                    .phoneNumber("1198888-8888")
                    .email(null)
                    .build();

            Customer result = CustomerJpaMapper.toDomain(entity);

            assertThat(result.getEmail()).isNull();
        }

        @Test
        void shouldReturnNullWhenEntityIsNull() {
            Customer result = CustomerJpaMapper.toDomain(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toEntity mapping")
    class ToEntity {

        @Test
        void shouldMapDomainToEntityWithEmail() {
            Customer domain = new Customer(
                    3L,
                    "Larissa Souza",
                    new Cpf("222.333.444-55"),
                    "1197777-7777",
                    new Email("larissa.souza@example.com")
            );

            CustomerJpaEntity result = CustomerJpaMapper.toEntity(domain);

            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getName()).isEqualTo("Larissa Souza");
            assertThat(result.getCpf()).isEqualTo("222.333.444-55");
            assertThat(result.getPhoneNumber()).isEqualTo("1197777-7777");
            assertThat(result.getEmail()).isEqualTo("larissa.souza@example.com");
        }

        @Test
        void shouldMapDomainToEntityWithoutEmail() {
            Customer domain = new Customer(
                    4L,
                    "João Silva",
                    new Cpf("111.222.333-44"),
                    "1196666-6666",
                    null
            );

            CustomerJpaEntity result = CustomerJpaMapper.toEntity(domain);

            assertThat(result.getEmail()).isNull();
        }

        @Test
        void shouldReturnNullWhenDomainIsNull() {
            CustomerJpaEntity result = CustomerJpaMapper.toEntity(null);
            assertThat(result).isNull();
        }
    }
}
