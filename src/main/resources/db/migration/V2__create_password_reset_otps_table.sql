create table password_reset_otps
(
    id         bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    otp_code   varchar(6)                          not null,
    expiry     timestamp                           not null,
    used       boolean   default false             not null,
    created_at timestamp default current_timestamp not null,
    constraint password_reset_otps_users_id_fk
        foreign key (user_id) references users (id)
);

