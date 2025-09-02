create table users
(
    id                 bigint auto_increment
        primary key,
    username           varchar(255)                       not null,
    email              varchar(255)                       not null,
    password           varchar(255)                       not null,
    role               varchar(20)                         not null,
    preferred_language varchar(5)                         not null,
    created_at         datetime default current_timestamp not null,
    updated_at         datetime default current_timestamp on update current_timestamp not null,
    constraint users_uk_1
        unique (email)
);

