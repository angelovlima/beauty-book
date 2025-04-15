package br.com.beauty_book.professional_management.domain.model.value_object;

import br.com.beauty_book.professional_management.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmailWhenValid() {
        var email = new Email("joao@example.com");

        assertThat(email.getValue()).isEqualTo("joao@example.com");
    }

    @Test
    void shouldReturnNullWhenEmailIsNull() {
        var email = new Email(null);
        assertThat(email.getValue()).isNull();
    }

    @Test
    void shouldReturnNullWhenEmailIsBlank() {
        var email = new Email("  ");
        assertThat(email.getValue()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThatThrownBy(() -> new Email("invalid-email"))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("invalid-email");

        assertThatThrownBy(() -> new Email("email@.com"))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("email@.com");

        assertThatThrownBy(() -> new Email("@example.com"))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("@example.com");
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        var email1 = new Email("ana@example.com");
        var email2 = new Email("ana@example.com");
        var email3 = new Email("bruna@example.com");

        assertThat(email1).isEqualTo(email2);
        assertThat(email1).isNotEqualTo(email3);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    void shouldReturnToStringEqualToValue() {
        var email = new Email("lucas@example.com");

        assertThat(email.toString()).isEqualTo("lucas@example.com");
    }
}
