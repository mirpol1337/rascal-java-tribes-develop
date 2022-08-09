drop table troop;
create table if not exists army
(
    id                bigint  not null
        constraint army_pkey
            primary key,
    arrived_at        bigint  not null,
    going_to_battle   boolean not null,
    returned_at       bigint  not null,
    started_at        bigint  not null,
    target_kingdom_id bigint,
    total_attack      integer not null,
    total_defense     integer not null,
    totalhp           integer not null,
    kingdom_id        bigint
        constraint fk1ntr1vx68tc378v84hwip135a
            references kingdom
);

create table if not exists troop
(
    id          bigint  not null
        constraint troop_pkey
            primary key,
    attack      integer not null,
    defense     integer not null,
    hp          integer not null,
    level       integer not null,
    started_at  bigint  not null,
    finished_at bigint  not null,
    kingdom_id  bigint
        constraint fk9c9dtfl3ehguq5d38u40c4c5w
            references kingdom,
    army_id     bigint
        constraint fk27q9ofr795nxkklvspvpv2iyg
            references army,
    is_in_army  boolean not null
);