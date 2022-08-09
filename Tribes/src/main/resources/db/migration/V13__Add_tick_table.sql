create table if not exists time
(
	id bigint not null,
	server_start bigint not null,
	ticks bigint not null,
	constraint time_pkey
		primary key (id)
);
insert into public.time (id, server_start, ticks) values (1, 1594123200, 1);