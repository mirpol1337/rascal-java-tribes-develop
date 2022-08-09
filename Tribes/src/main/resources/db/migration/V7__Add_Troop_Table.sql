create table if not exists troop
(
    id           bigint  not null
        constraint troop_pkey
            primary key,
    attack       integer not null,
    defense      integer not null,
    finished_at bigint  not null,
    hp           integer not null,
    level        integer not null,
    started_at   bigint  not null
);