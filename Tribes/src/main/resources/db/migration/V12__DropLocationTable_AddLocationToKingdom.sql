drop table location cascade;

alter table kingdom
add     x integer not null;
alter table kingdom
add     y integer not null;

alter table kingdom drop column location_id;