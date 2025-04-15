package br.com.beauty_book.establishment_management.domain.model;

import br.com.beauty_book.establishment_management.domain.exception.EmptyReviewCommentException;
import br.com.beauty_book.establishment_management.domain.exception.InvalidReviewStarsException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Review {

    private final Long id;
    private final Long customerId;
    private final Long establishmentId;
    private final int stars;
    private final String comment;

    public Review(Long id, Long customerId, Long establishmentId, int stars, String comment) {
        if (stars < 1 || stars > 5) {
            throw new InvalidReviewStarsException();
        }
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId);
        this.establishmentId = Objects.requireNonNull(establishmentId);
        this.stars = stars;
        this.comment = Objects.requireNonNull(comment).trim();
        if (this.comment.isEmpty()) {
            throw new EmptyReviewCommentException();
        }
    }
}
