create table clients
(
    id        serial primary key,
    full_name varchar(255) not null
);

create table cars
(
    id         serial primary key,
    number_car varchar(9) not null unique,
    clients_id integer references clients (id)  on delete set null
);

create table parking_spaces
(
    id           serial primary key,
    number_space varchar(15) not null unique check (left (number_space, 2) = 'N_')
    );

create table reservations
(
    id         serial primary key,
    parking_id integer references parking_spaces (id) on delete cascade,
    car_id     integer references cars (id) on delete cascade,
    is_paid    boolean default false
);


alter table reservations
    add column start_time timestamp with time zone not null,
    add column add_time timestamp with time zone not null;

alter table cars rename column clients_id to client_id;

alter table reservations
    alter column end_time drop not null;