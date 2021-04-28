create table "user"
(
    id uuid not null
        constraint user_pk
            primary key,
    login varchar(30) not null,
    full_name varchar(50) not null,
    email varchar(320) not null,
    password text not null,
    role varchar(10) not null
);

alter table "user" owner to datagrip;

create unique index user_email_uindex
	on "user" (email);

create unique index user_id_uindex
	on "user" (id);

create unique index user_login_uindex
	on "user" (login);

