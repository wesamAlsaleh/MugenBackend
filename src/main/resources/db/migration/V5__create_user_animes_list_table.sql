create table user_animes_list
(
    id         bigint auto_increment
        primary key,
    user_id    bigint                              not null,
    anime_id   bigint                              not null comment 'From AniList API',
    status     varchar(15)                         not null,
    progress   int       default 0                 not null comment 'Episodes watched',
    score      int                                 null comment 'Optional rating',
    started_at timestamp default current_timestamp not null comment 'When user started watching',
    updated_at timestamp default current_timestamp not null on update current_timestamp comment 'Last update',
    constraint user_animes_list_uk
        unique (user_id, anime_id),
    constraint user_animes_list_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

create index user_animes_list__index_2
    on user_animes_list (user_id, status)
    comment 'Look up a user’s anime list filtered by status (e.g. WATCHING, DROPPED)';

create index user_animes_list_anime_id_index
    on user_animes_list (anime_id)
    comment 'Look up all users who have a certain anime';

