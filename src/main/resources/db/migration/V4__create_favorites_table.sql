create table favorites
(
    id         bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    anime_id   bigint                              not null,
    created_at timestamp default current_timestamp not null,
    constraint favorites_user_anime_uk
        unique (user_id, anime_id),
    constraint favorites_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

