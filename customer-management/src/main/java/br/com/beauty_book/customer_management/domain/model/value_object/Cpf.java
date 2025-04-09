package br.com.beauty_book.customer_management.domain.model.value_object;

import br.com.beauty_book.customer_management.domain.exception.InvalidCpfException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Cpf {

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");

    private final String value;

    public Cpf(String value) {
        this.value = validate(value);
    }

    private String validate(String cpf) {
        Objects.requireNonNull(cpf, "CPF must not be null");
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            throw new InvalidCpfException(cpf);
        }
        return cpf;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf cpf)) return false;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

