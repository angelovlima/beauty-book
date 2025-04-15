CREATE TABLE IF NOT EXISTS professional (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS availability (
    id BIGSERIAL PRIMARY KEY,
    professional_id BIGINT NOT NULL,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_availability_professional FOREIGN KEY (professional_id)
    REFERENCES professional (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS professional_service_offered (
    id BIGSERIAL PRIMARY KEY,
    professional_id BIGINT NOT NULL,
    service_offered_id BIGINT NOT NULL,
    CONSTRAINT fk_professional_service_professional FOREIGN KEY (professional_id)
    REFERENCES professional (id) ON DELETE CASCADE
);
