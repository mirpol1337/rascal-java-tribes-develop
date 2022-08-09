create table if not exists building
(
	id bigint not null,
	finished_at bigint not null,
	hp integer not null,
	level integer not null,
	started_at bigint not null,
	type varchar(255),
	constraint building_pkey
		primary key (id)
);
