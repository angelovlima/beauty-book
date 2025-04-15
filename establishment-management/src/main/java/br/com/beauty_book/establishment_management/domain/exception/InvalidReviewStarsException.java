package br.com.beauty_book.establishment_management.domain.exception;

public class InvalidReviewStarsException extends RuntimeException {
    public InvalidReviewStarsException() {
        super("A avaliação deve ter entre 1 e 5 estrelas.");
    }
}
