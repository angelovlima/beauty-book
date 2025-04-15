package br.com.beauty_book.professional_management.domain.model.value_object;

import br.com.beauty_book.professional_management.domain.exception.InvalidCpfException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CpfTest {

    @Test
    void shouldCreateCpfWhenValid() {
        var cpf = new Cpf("123.456.789-00");

        assertThat(cpf.getValue()).isEqualTo("123.456.789-00");
    }

    @Test
    void shouldThrowExceptionWhenCpfIsInvalid() {
        assertThatThrownBy(() -> new Cpf("12345678900"))
                .isInstanceOf(InvalidCpfException.class)
                .hasMessageContaining("12345678900");

        assertThatThrownBy(() -> new Cpf("abc.def.ghi-jk"))
                .isInstanceOf(InvalidCpfException.class)
                .hasMessageContaining("abc.def.ghi-jk");
    }

    @Test
    void shouldThrowExceptionWhenCpfIsNull() {
        assertThatThrownBy(() -> new Cpf(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        var cpf1 = new Cpf("123.456.789-00");
        var cpf2 = new Cpf("123.456.789-00");
        var cpf3 = new Cpf("999.999.999-99");

        assertThat(cpf1).isEqualTo(cpf2);
        assertThat(cpf1).isNotEqualTo(cpf3);
        assertThat(cpf1.hashCode()).isEqualTo(cpf2.hashCode());
    }

    @Test
    void shouldReturnToStringEqualToValue() {
        var cpf = new Cpf("123.456.789-00");

        assertThat(cpf.toString()).isEqualTo("123.456.789-00");
    }
}
