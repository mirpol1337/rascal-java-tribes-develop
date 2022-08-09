drop table application_user;
create table if not exists application_user
(
     id         uuid not null
        constraint application_user_pkey
            primary key,
    name       varchar(255),
    password   varchar(255),
    kingdom_id bigint
        constraint fkep6vooglihwraille12muox9
            references kingdom
);
drop table building;
create table if not exists building
(
    id          bigint  not null
        constraint building_pkey
            primary key,
    finished_at bigint  not null,
    hp          integer not null,
    level       integer not null,
    started_at  bigint  not null,
    type        varchar(255),
    kingdom_id  bigint
        constraint fk6s58l4oxb2hntomgyhe5byvpa
            references kingdom
);
drop table troop;
create table if not exists troop
(
    id           bigint  not null
        constraint troop_pkey
            primary key,
    attack       integer not null,
    defense      integer not null,
    hp           integer not null,
    level        integer not null,
    started_at   bigint  not null,
    finished_at  bigint  not null,
    kingdom_id   bigint
        constraint fk9c9dtfl3ehguq5d38u40c4c5w
            references kingdom
);
alter table kingdom
add  user_id     uuid
        constraint fk6f4qbycowqfcrnsc1s6ffnc11
            references application_user
;
alter table resource
add     kingdom_id bigint
        constraint fks4wjv1ijb0jucakvtlupiuxqh
            references kingdom

            ;
