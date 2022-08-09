create table if not exists application_user
(
    id       uuid not null
        constraint application_user_pkey
            primary key,
    kingdom  varchar(255),
    name     varchar(255),
    password varchar(255)
);