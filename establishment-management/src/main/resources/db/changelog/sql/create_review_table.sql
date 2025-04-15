CREATE TABLE IF NOT EXISTS review (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    establishment_id BIGINT NOT NULL,
    stars INTEGER NOT NULL CHECK (stars BETWEEN 1 AND 5),
    comment TEXT NOT NULL,
    CONSTRAINT fk_review_establishment FOREIGN KEY (establishment_id)
    REFERENCES establishment (id) ON DELETE CASCADE,
    CONSTRAINT uk_customer_establishment UNIQUE (customer_id, establishment_id)
);
