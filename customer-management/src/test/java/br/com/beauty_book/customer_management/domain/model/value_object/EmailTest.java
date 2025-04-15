package br.com.beauty_book.customer_management.domain.model.value_object;

import br.com.beauty_book.customer_management.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailTest {

    @Nested
    @DisplayName("When creating Email")
    class Create {

        @Test
        @DisplayName("should create valid email")
        void shouldCreateValidEmail() {
            Email email = new Email("ana.silva@example.com");
            assertThat(email.getValue()).isEqualTo("ana.silva@example.com");
        }

        @Test
        @DisplayName("should throw exception for invalid email format")
        void shouldThrowForInvalidEmail() {
            assertThatThrownBy(() -> new Email("email-invalido"))
                    .isInstanceOf(InvalidEmailException.class)
                    .hasMessageContaining("email-invalido");
        }

        @Test
        @DisplayName("should allow null email")
        void shouldAllowNullEmail() {
            Email email = new Email(null);
            assertThat(email.getValue()).isNull();
        }

        @Test
        @DisplayName("should allow blank email")
        void shouldAllowBlankEmail() {
            Email email = new Email("   ");
            assertThat(email.getValue()).isNull();
        }
    }

    @Nested
    @DisplayName("When comparing Email objects")
    class Compare {

        @Test
        @DisplayName("should be equal when values match")
        void shouldBeEqualIfValuesMatch() {
            Email e1 = new Email("ana.silva@example.com");
            Email e2 = new Email("ana.silva@example.com");

            assertThat(e1).isEqualTo(e2);
            assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        }

        @Test
        @DisplayName("should not be equal when values differ")
        void shouldNotBeEqualIfDifferent() {
            Email e1 = new Email("ana.silva@example.com");
            Email e2 = new Email("joao.lima@example.com");

            assertThat(e1).isNotEqualTo(e2);
        }

        @Test
        @DisplayName("should return correct string representation")
        void shouldReturnToString() {
            Email email = new Email("carla.rocha@example.com");
            assertThat(email.toString()).isEqualTo("carla.rocha@example.com");
        }
    }
}
