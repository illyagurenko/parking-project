CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    number_car VARCHAR(9) NOT NULL UNIQUE,
    client_id INTEGER REFERENCES clients (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS parking_spaces (
    id SERIAL PRIMARY KEY,
    number_space VARCHAR(15) NOT NULL UNIQUE CHECK (LEFT(number_space, 2) = 'N_')
    );

CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    parking_id INTEGER REFERENCES parking_spaces (id) ON DELETE CASCADE,
    car_id INTEGER REFERENCES cars (id) ON DELETE CASCADE,
    is_paid BOOLEAN DEFAULT FALSE,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE
        );
