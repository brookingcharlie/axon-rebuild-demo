create sequence global_event_index_seq start with 1 increment by 1;
create sequence hibernate_sequence start with 1 increment by 1;

create table association_value_entry (
    id bigint not null,
    association_key varchar(255) not null,
    association_value varchar(255),
    saga_id varchar(255) not null,
    saga_type varchar(255),
    primary key (id)
);

create table domain_event_entry (
    global_index bigint not null,
    event_identifier varchar(255) not null,
    meta_data blob,
    payload blob not null,
    payload_revision varchar(255),
    payload_type varchar(255) not null,
    time_stamp varchar(255) not null,
    aggregate_identifier varchar(255) not null,
    sequence_number bigint not null,
    type varchar(255),
    primary key (global_index)
);

create table saga_entry (
    saga_id varchar(255) not null,
    revision varchar(255),
    saga_type varchar(255),
    serialized_saga blob,
    primary key (saga_id)
);

create table snapshot_event_entry (
    aggregate_identifier varchar(255) not null,
    sequence_number bigint not null,
    type varchar(255) not null,
    event_identifier varchar(255) not null,
    meta_data blob,
    payload blob not null,
    payload_revision varchar(255),
    payload_type varchar(255) not null,
    time_stamp varchar(255) not null,
    primary key (aggregate_identifier, sequence_number, type)
);

create table token_entry (
    processor_name varchar(255) not null,
    segment integer not null,
    owner varchar(255),
    timestamp varchar(255) not null,
    token blob,
    token_type varchar(255),
    primary key (processor_name, segment)
);

create index idx2uqqpmht3w2i368ld2ham2out on association_value_entry (saga_type, association_key, association_value);

create index idxpo4uvnt1l3922m6y62fk73p3f on association_value_entry (saga_id, saga_type);

alter table domain_event_entry
    add constraint ukdg43ia27ypo1jovw2x64vbwv8 unique (aggregate_identifier, sequence_number);

alter table domain_event_entry
    add constraint uk_k5lt6d2792amnloo7q91njp0v unique (event_identifier);

alter table snapshot_event_entry
    add constraint uk_sg7xx45yh4ajlsjd8t0uygnjn unique (event_identifier);
