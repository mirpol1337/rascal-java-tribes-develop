create table if not exists log
(
    id              bigint  not null
        constraint log_pkey
            primary key,
    created         timestamp,
    error_message   varchar(255),
    error_status    integer,
    execution_time  integer not null,
    logging_lvl     varchar(255),
    request_method  varchar(255),
    request_string  varchar(1024),
    response_status integer not null,
    response_string varchar(1024)
);