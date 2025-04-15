package br.com.beauty_book.customer_management.domain.model.value_object;

import br.com.beauty_book.customer_management.domain.exception.InvalidCpfException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CpfTest {

    @Nested
    @DisplayName("When creating CPF")
    class Create {

        @Test
        @DisplayName("should create valid CPF")
        void shouldCreateValidCpf() {
            Cpf cpf = new Cpf("123.456.789-00");
            assertThat(cpf.getValue()).isEqualTo("123.456.789-00");
        }

        @Test
        @DisplayName("should throw exception for invalid CPF")
        void shouldThrowForInvalidCpf() {
            assertThatThrownBy(() -> new Cpf("12345678900"))
                    .isInstanceOf(InvalidCpfException.class)
                    .hasMessageContaining("12345678900");
        }

        @Test
        @DisplayName("should throw exception for null CPF")
        void shouldThrowForNullCpf() {
            assertThatThrownBy(() -> new Cpf(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("CPF must not be null");
        }
    }

    @Nested
    @DisplayName("When comparing CPF objects")
    class Compare {

        @Test
        @DisplayName("should be equal when values match")
        void shouldBeEqualIfValuesMatch() {
            Cpf c1 = new Cpf("123.456.789-00");
            Cpf c2 = new Cpf("123.456.789-00");

            assertThat(c1).isEqualTo(c2);
            assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
        }

        @Test
        @DisplayName("should not be equal when values differ")
        void shouldNotBeEqualIfDifferent() {
            Cpf c1 = new Cpf("123.456.789-00");
            Cpf c2 = new Cpf("111.222.333-44");

            assertThat(c1).isNotEqualTo(c2);
        }

        @Test
        @DisplayName("should return correct string representation")
        void shouldReturnToString() {
            Cpf cpf = new Cpf("321.654.987-00");
            assertThat(cpf.toString()).isEqualTo("321.654.987-00");
        }
    }
}
