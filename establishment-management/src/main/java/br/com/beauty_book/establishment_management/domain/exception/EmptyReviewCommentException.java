package br.com.beauty_book.establishment_management.domain.exception;

public class EmptyReviewCommentException extends RuntimeException {
    public EmptyReviewCommentException() {
        super("O comentário da avaliação não pode estar vazio.");
    }
}
