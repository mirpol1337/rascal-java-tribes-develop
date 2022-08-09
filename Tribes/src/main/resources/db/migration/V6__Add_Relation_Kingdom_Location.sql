alter table kingdom

        add location_id bigint
        constraint fkamafacqsrycbhgwn8a7cut4cl
            references location
;

alter table location

        add kingdom_id bigint
        constraint fkihvvwuhcte0kx4ymeuqg22xim
            references kingdom
;
