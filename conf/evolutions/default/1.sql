
# --- !Ups

create table hotels(
    id  integer primary key not null,
    city varchar(1023),
    name varchar(1023),
    unique(city,name)
);

create table availabilities(
    hotel_id integer references hotels (id),
    date date not null,
    available boolean not null
);

# --- !Downs