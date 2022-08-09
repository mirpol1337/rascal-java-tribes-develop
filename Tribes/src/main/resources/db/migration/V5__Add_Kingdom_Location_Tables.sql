create table kingdom
(
    id          bigint not null
        constraint kingdom_pkey
            primary key,
    name        varchar(255)

);

create table location
(
    id         bigint  not null
        constraint location_pkey
            primary key,
    x          integer not null,
    y          integer not null

);