alter table password_reset_otps
    modify expiry datetime not null;

alter table password_reset_otps
drop foreign key password_reset_otps_users_id_fk;

alter table password_reset_otps
    add constraint password_reset_otps_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade;

