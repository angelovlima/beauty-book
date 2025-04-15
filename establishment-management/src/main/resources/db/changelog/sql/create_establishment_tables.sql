CREATE TABLE IF NOT EXISTS establishment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    photo_url TEXT
);

CREATE TABLE IF NOT EXISTS operating_hour (
    id BIGSERIAL PRIMARY KEY,
    establishment_id BIGINT NOT NULL,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT fk_operating_hour_establishment FOREIGN KEY (establishment_id)
    REFERENCES establishment (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS service_offered (
    id BIGSERIAL PRIMARY KEY,
    establishment_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    duration_minutes INT NOT NULL,
    CONSTRAINT fk_service_establishment FOREIGN KEY (establishment_id)
    REFERENCES establishment (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS professional_establishment (
    id BIGSERIAL PRIMARY KEY,
    establishment_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    CONSTRAINT fk_professional_establishment FOREIGN KEY (establishment_id)
    REFERENCES establishment (id) ON DELETE CASCADE
);
