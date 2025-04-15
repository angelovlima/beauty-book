CREATE TABLE IF NOT EXISTS booking (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    establishment_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL
);
